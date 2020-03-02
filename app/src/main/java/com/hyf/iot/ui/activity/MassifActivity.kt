package com.hyf.iot.ui.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.MassifContract
import com.hyf.iot.domain.LatLonData
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.presenter.MassifAddOrEditPresenter
import com.hyf.iot.utils.mapCeshi.CombinationOverlay
import com.hyf.iot.utils.mapCeshi.MapUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.dialog.NormalMsgDialog
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.map_layout.*
import kotlinx.android.synthetic.main.map_layout.mMapView
import kotlin.math.abs

/**
 * 地块
 *
 * */
@SuppressLint("SetTextI18n")
class MassifActivity : BaseMvpActivity<MassifContract.IPresenter>(), MassifContract.IView {

    private var mBaiduMap: BaiduMap? = null
    /**当前物理坐标 */
    private var mCenterPoint: Point? = null
    /**当前地理坐标 */
    private var mLoactionLatLng: LatLng? = null
    // 农场信息
    private var farmDetail: Farm? = null
    internal var mLatitude = 30.654008
    internal var mLongtitude = 104.093591

    private var combinationOverlay: CombinationOverlay? = null// 当前选中的覆盖物

    private var bitmap: BitmapDescriptor = BitmapDescriptorFactory
    .fromResource(R.drawable.icon_dot_anchor)
    private var currentPosition: Int = -1 // 当前点
    private var mList: ArrayList<LatLng> = ArrayList() // 点坐标集合
    private var mMarkerList: ArrayList<LatLonData> = ArrayList()
    private var isArea: Boolean = false
    private var id = ""  // 地块ID

    override fun getLayoutId(): Int = R.layout.map_layout

    override fun initData() {
        super.initData()
        if (LoginUser.farmId.isNullOrEmpty()) {
            val dialog = NormalMsgDialog(this).setMessage("当前无农场信息，是否先新建农场？")
                    .setRightButtonInfo("", DialogInterface.OnClickListener { _, _ ->
                        newIntent<FarmAddOrEditActivity>()
                    })
            dialog.show()
        } else {
            getPresenter().getFarmDetail(LoginUser.farmId)
        }
    }

    override fun initView() {
        tv_title.text = getString(R.string.add_massif)
        tv_operate.visibility = View.VISIBLE
        tv_operate.text = "确定"
        tv_operate.setOnClickListener {
            val massifName = et_name.text.toString()
            if (massifName.isEmpty()) {
                et_name.requestFocus()
                showToast("请输入地块名")
                return@setOnClickListener
            }
            val massifSize = area.text.toString().toFloat()
            if (massifSize <= 0f) {
                return@setOnClickListener
            }
            getPresenter().massifAdd(LoginUser.farmId, massifName, area.text.toString().toFloat(), mList)
        }
        iv_back.setOnClickListener { onBack() }
        combinationOverlay = CombinationOverlay(mMapView!!)
        initBaiduMap() //init为定位方法

        delete.setOnClickListener {
            if (currentPosition == -1) {
                Toast.makeText(this, "当前无选中点", Toast.LENGTH_SHORT).show()
            } else {
                mMarkerList[currentPosition].marker.remove()
                mMarkerList.removeAt(currentPosition)
                mList.removeAt(currentPosition)
                if (mList.size >= 3) {
                    pl?.points = mList
                    pLine?.points = mList
                } else {
                    pl?.remove()
                    pLine?.remove()
                    drawLine()
                    isArea = false
                }
                currentPosition = if (mMarkerList.size > 0) {
                    mMarkerList.size - 1
                } else {
                    -1
                }
                pointView?.visibility = View.GONE
                area.text = MapUtils.getArea(pl)
            }
        }
    }

    override fun registerPresenter() = MassifAddOrEditPresenter::class.java

    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun onError(errorMsg: String?) {
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }

    override fun addSuccess() {
        showToast("添加成功")
        setResult(RESULT_OK)
        finish()
    }

    override fun editSuccess() {
        showToast("编辑成功")
        setResult(RESULT_OK)
        finish()
    }

    override fun showFarmDetail(data: Farm) {
        farmDetail = data
        mLatitude = data.latitude
        mLongtitude = data.longitude
        mLoactionLatLng = LatLng(mLatitude, mLongtitude)
        mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
        initMarker(mLoactionLatLng!!)
    }

    private fun initMarker(point: LatLng) {
        val bdA: BitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_farm_loc)
        val ooA = MarkerOptions().position(point).icon(bdA)
        val mMarkerA = mBaiduMap!!.addOverlay(ooA)
        val mBundle = Bundle()
        mBundle.putParcelable("item", farmDetail)
        mMarkerA.extraInfo = mBundle
    }

    @Suppress("DEPRECATION")
    private fun initBaiduMap() {
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(15f))
        mBaiduMap!!.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                if (!isArea) {
                    combinationOverlay!!.drawPoint(p0)
                    combinationOverlay!!.drawLine()
                    initMarker(mLoactionLatLng!!)
                }
            }
            override fun onMapPoiClick(p0: MapPoi?): Boolean {
                return true
            }
        })

        mBaiduMap!!.setOnMarkerClickListener { marker ->
            mMarkerList = combinationOverlay!!.getLatLngMarkerList()!!
            mList = combinationOverlay!!.getLatLngList() as ArrayList<LatLng>
            if (mMarkerList[0].marker == marker && !isArea && mMarkerList.size > 2) {
                isArea = true
                pLine?.remove()
                pl = combinationOverlay!!.drawArea()
                combinationOverlay!!.drawPoints()
                area.text = MapUtils.getArea(pl) // 面积
            } else {
                setPointView(marker)
            }
            true
        }

        mBaiduMap!!.setOnMapStatusChangeListener(object : BaiduMap.OnMapStatusChangeListener {
            override fun onMapStatusChangeStart(p0: MapStatus?) {
            }

            override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
            }

            override fun onMapStatusChange(p0: MapStatus?) {
                if (pointView?.visibility == View.VISIBLE) {
                    pointView?.visibility = View.GONE
                }
            }
            override fun onMapStatusChangeFinish(p0: MapStatus?) {
            }
        })
    }

    private fun setPointView(marker: Marker) {
        mMarkerList = combinationOverlay!!.getLatLngMarkerList()!!
        for (i in mMarkerList.indices) {
            if (marker == mMarkerList[i].marker) {
                currentPosition = i
            }
        }
        val point = mBaiduMap!!.projection.toScreenLocation(marker.position)
        addPointView(point.x, point.y)
    }


    private fun twoLineAutoAddPoint() {
        mList = combinationOverlay!!.getLatLngList()!! as ArrayList<LatLng>
        mMarkerList = combinationOverlay!!.getLatLngMarkerList()!!
        if (isArea) {
            if (mMarkerList[currentPosition].isAddPoint) return
            if (currentPosition != -1) {
                val latLng0 = if (currentPosition == 0) {
                    mList[mList.size - 1]
                } else {
                    mList[currentPosition - 1]
                }
                val latLng1 = mList[currentPosition]
                val latLng2 = if (currentPosition == mList.size - 1) {
                    mList[0]
                } else {
                    mList[currentPosition + 1]
                }
                val latLngL = combinationOverlay!!.getMidPoint(latLng0, latLng1)
                val latLngR = combinationOverlay!!.getMidPoint(latLng1, latLng2)
                mList.add(currentPosition + 1, latLngR)
                mList.add(currentPosition, latLngL)

                mMarkerList.add(currentPosition + 1, LatLonData(addMarker(latLngR), false))
                mMarkerList.add(currentPosition, LatLonData(addMarker(latLngL), false))
                pl?.remove()
                pl = combinationOverlay!!.drawArea()
                combinationOverlay!!.drawPoints()
                currentPosition += 1
                mMarkerList[currentPosition].isAddPoint = true
            }
        }
        combinationOverlay!!.setLatLngList(mList)
        combinationOverlay!!.setLatLngMarkerList(mMarkerList)
    }

    private fun drawArea(): Polygon {
        val mPolygonOptions = PolygonOptions()
                .points(mList)
                .fillColor(0x551791fc) //填充颜色
                .stroke(Stroke(6, 0x55FF33FF)) //边框宽度和颜色
        convertViewToBitmap(toScreenPoints()!!)
        return mBaiduMap!!.addOverlay(mPolygonOptions) as Polygon
    }


    private fun convertViewToBitmap(points: ArrayList<Point>): Bitmap {
        var paint: Paint = Paint()
        //设置一个笔刷大小是5的黄色的画笔
        paint.color = Color.YELLOW
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 5f
        val path = Path()
        path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
        for (i in 1 until points.size) {
            path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
        }
        path.close() // 封闭
        val bitmap = Bitmap.createBitmap(mMapView.width, mMapView.height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPath(path, paint)
        bitview.setImageBitmap(bitmap)
        return bitmap
    }


    private fun toScreenPoints(): ArrayList<Point>?{
        if (mList.size == 0) return null
        val points = arrayListOf<Point>()
        for (i in 0 until mList.size) {
            points.add(mBaiduMap!!.projection.toScreenLocation(mList[i]))
        }
        return points
    }

    private fun addMarker(latLng: LatLng): Marker {
        val option: MarkerOptions = MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .anchor(0.5f, 0.5f)
        return mBaiduMap!!.addOverlay(option) as Marker
    }

    private var pointView: ImageView? = null
    @SuppressLint("ClickableViewAccessibility")
    private fun addPointView(x: Int, y: Int) {
        mList = combinationOverlay!!.getLatLngList() as ArrayList<LatLng>
        if (pointView == null) {
            pointView = ImageView(this)
            pointView?.setImageResource(R.drawable.icon_arrow_drag)
            layout.addView(pointView)
            val layoutp = pointView?.layoutParams
            layoutp?.width = 80
            layoutp?.height = 80
            pointView?.layoutParams = layoutp
            var startX = 0f
            var startY = 0f
            var endX: Float
            var endY: Float
            pointView?.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX
                        startY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        v.x = v.x + event.rawX - startX
                        v.y = v.y + event.rawY - startY
                        val point = Point(v.x.toInt(), v.y.toInt())
                        addPointView(v.x.toInt(), v.y.toInt())
                        val latLng: LatLng = mBaiduMap!!.projection.fromScreenLocation(point)
                        mMarkerList[currentPosition].marker.position = latLng
                        mList[currentPosition] = latLng
                        pl?.points = mList
                        startX = event.rawX
                        startY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                        pl?.points = mList
                        twoLineAutoAddPoint()
                        area.text = MapUtils.getArea(pl)
                        endX = event.rawX
                        endY = event.rawY
                        if (abs(endX - startX) < 5 && abs(endY - startY) < 5) false
//                        convertViewToBitmap(toScreenPoints()!!)
                    }
                }
                true
            }
        }
        combinationOverlay!!.setLatLngList(mList)
        combinationOverlay!!.setLatLngMarkerList(mMarkerList)
        pointView?.x = x.toFloat()
        pointView?.y = y.toFloat()
        pointView?.visibility = View.VISIBLE
    }

    private fun drawPoint(p0: LatLng?) {
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_dot_anchor)
        val option: MarkerOptions = MarkerOptions()
                .position(p0)
                .icon(bitmap)
                .anchor(0.5f, 0.5f)
        val marker = mBaiduMap!!.addOverlay(option) as Marker
        mMarkerList.add(LatLonData(marker, false))
        if (p0 != null) {
            mList.add(p0)
            currentPosition = mList.size - 1
        }
    }

    private fun drawPoints() {
        mMarkerList.forEach {
            it.marker.remove()
        }
        mMarkerList.clear()
        //线段中间加小锚点
        mList.forEach {
            val option: MarkerOptions = MarkerOptions()
                    .position(it)
                    .icon(bitmap)
                    .anchor(0.5f, 0.5f)
            val marker = mBaiduMap!!.addOverlay(option) as Marker
            mMarkerList.add(LatLonData(marker, false))
        }
    }

    private fun drawLine() {
        mBaiduMap!!.clear()
        mMarkerList.clear()
        if (mList.size >= 2) {
            //在地图上绘制折线
            //mPloyline 折线对象
            val mOverlayOptions = PolylineOptions()
                    .width(6)
                    .color(0x55FF33FF)
                    .points(mList)
            pLine = mBaiduMap!!.addOverlay(mOverlayOptions) as Polyline
        }

        mList.forEach {
            val option: MarkerOptions = MarkerOptions()
                    .position(it)
                    .icon(bitmap)
                    .anchor(0.5f, 0.5f)
            val marker = mBaiduMap!!.addOverlay(option) as Marker
            mMarkerList.add(LatLonData(marker, false))
        }
        initMarker(mLoactionLatLng!!)
    }

    private var pLine: Polyline? = null
    private var pl: Polygon? = null


    override fun onPause() {
        super.onPause()
        // activity 暂停时同时暂停地图控件
        mMapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        // activity 恢复时同时恢复地图控件
        mMapView!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        when {
            mBaiduMap != null -> // 关闭定位图层
                mBaiduMap!!.isMyLocationEnabled = false
            mMapView != null -> mMapView!!.onDestroy()
        }
    }
}