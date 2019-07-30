package com.hyf.iot.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.MassifContract
import com.hyf.iot.domain.LatLonData
import com.hyf.iot.presenter.MassifAddOrEditPresenter
import com.hyf.iot.utils.mapCeshi.MapUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.map_layout.*
import kotlin.math.abs
import android.graphics.Bitmap.createBitmap as createBitmap1

/**
 * 地块
 *
 * */
@SuppressLint("SetTextI18n")
class MassifActivity : BaseMvpActivity<MassifContract.IPresenter>(), MassifContract.IView {

    private var mBaiduMap: BaiduMap? = null
    var myListener = MyLocationListenner()
    private var mLocClient: LocationClient? = null
    private val BAIDU_READ_PHONE_STATE = 100
    internal var isFirstLoc = true // 是否首次定位
    private lateinit var bitmap: BitmapDescriptor

    private var pos: Int = -1 // 当前点
    private var mList: ArrayList<LatLng> = ArrayList() // 点坐标集合
    private var mMarkerList: ArrayList<LatLonData> = ArrayList()
    private var isArea: Boolean = false
    private var id = ""  // 地块ID

    override fun getLayoutId(): Int = R.layout.map_layout

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
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(15f))
        //检查权限
        //判断是否为android6.0系统版本，如果是，需要动态添加权限
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts()
        } else {
            initBaidu() //init为定位方法
        }

        delete.setOnClickListener {
            if (pos == -1) {
                Toast.makeText(this, "当前无选中点", Toast.LENGTH_SHORT).show()
            } else {
                mMarkerList[pos].marker.remove()
                mMarkerList.removeAt(pos)
                mList.removeAt(pos)
                if (mList.size >= 3) {
                    pl?.points = mList
                    pLine?.points = mList
                } else {
                    pl?.remove()
                    pLine?.remove()
                    drawLineor()
                    isArea = false
                }
                pos = if (mMarkerList.size > 0) {
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

    private fun showContacts() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            showToast("没有权限,请手动开启定位权限")
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            this.let {
                ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE
                        ),
                        BAIDU_READ_PHONE_STATE
                )
            }
        } else {
            initBaidu()
        }
    }

    @Suppress("DEPRECATION")
    private fun initBaidu() {
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        // 定位初始化
        mLocClient = LocationClient(this)
        mLocClient!!.registerLocationListener(myListener)
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(0)
        mLocClient!!.locOption = option
        mLocClient!!.start()

        mBaiduMap!!.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(p0: LatLng?) {
                if (!isArea) {
                    drawPoint(p0)
                    drawLineor()
                }
            }

            override fun onMapPoiClick(p0: MapPoi?): Boolean {
                return true
            }
        })

        mBaiduMap!!.setOnMarkerClickListener { marker ->
            if (mMarkerList[0].marker == marker) {
                if (!isArea) {
                    if (mMarkerList.size > 2) {
                        isArea = true
                        pLine?.remove()
//                        allLineAutoAddPoint()
                        pl = drawArea()
                        drawPoints()
                        area.text = MapUtils.getArea(pl) // 面积
                    } else {
                        setPointView(marker)
                    }
                } else {
                    setPointView(marker)
                }
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
        for (i in mMarkerList.indices) {
            if (marker == mMarkerList[i].marker) {
                pos = i
            }
        }
        val point = mBaiduMap!!.projection.toScreenLocation(marker.position)
        addPointView(point.x, point.y)
    }

    private fun allLineAutoAddPoint() {
        val nList = ArrayList<LatLng>()
        for (i in 0 until mList.size) {
            if (i == mList.size - 1) {
                val l = mList[mList.size - 1]
                val r = mList[0]
                val m = getMidPoint(l, r)
                nList.add(m)
            } else {
                val l = mList[i]
                val r = mList[i + 1]
                val m = getMidPoint(l, r)
                nList.add(m)
            }
        }

        for (i in mList.size - 1 downTo 0) {
            mList.add(i + 1, nList[i])
        }
    }

    private fun twoLineAutoAddPoint() {
        if (isArea) {
            if (mMarkerList[pos].isAddPoint) return
            if (pos != -1) {
                val latLng0 = if (pos == 0) {
                    mList[mList.size - 1]
                } else {
                    mList[pos - 1]
                }
                val latLng1 = mList[pos]
                val latLng2 = if (pos == mList.size - 1) {
                    mList[0]
                } else {
                    mList[pos + 1]
                }
                val latLngL = getMidPoint(latLng0, latLng1)
                val latLngR = getMidPoint(latLng1, latLng2)
                mList.add(pos + 1, latLngR)
                mList.add(pos, latLngL)

                mMarkerList.add(pos + 1, LatLonData(addMarker(latLngR), false))
                mMarkerList.add(pos, LatLonData(addMarker(latLngL), false))
                pl?.remove()
                pl = drawArea()
                drawPoints()
                pos += 1
                mMarkerList[pos].isAddPoint = true
            }

        }
    }

    private fun drawArea(): Polygon {
        val mPolygonOptions = PolygonOptions()
                .points(mList)
                .fillColor(0x551791fc) //填充颜色
                .stroke(Stroke(6 , 0x55FF33FF)) //边框宽度和颜色

        return mBaiduMap!!.addOverlay(mPolygonOptions) as Polygon
    }

    private fun addMarker(latLng: LatLng): Marker {
        val option: MarkerOptions = MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .anchor(0.5f, 0.5f)
        return mBaiduMap!!.addOverlay(option) as Marker
    }

    private fun getMidPoint(latLng0: LatLng, latLng1: LatLng): LatLng {
        val dx = if (latLng0.latitude - latLng1.latitude > 0) {
            abs(latLng0.latitude - latLng1.latitude) / 2 + latLng1.latitude
        } else {
            abs(latLng0.latitude - latLng1.latitude) / 2 + latLng0.latitude
        }

        val dy = if (latLng0.longitude - latLng1.longitude > 0) {
            abs(latLng0.longitude - latLng1.longitude) / 2 + latLng1.longitude
        } else {
            abs(latLng0.longitude - latLng1.longitude) / 2 + latLng0.longitude
        }
        return LatLng(dx, dy)
    }

    private var pointView: ImageView? = null
    @SuppressLint("ClickableViewAccessibility")
    private fun addPointView(x: Int, y: Int) {
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
                        mMarkerList[pos].marker.position = latLng
                        mList[pos] = latLng
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
                    }
                }
                true
            }
        }
        pointView?.x = x.toFloat()
        pointView?.y = y.toFloat()
        pointView?.visibility = View.VISIBLE
    }

    private var lPos: Int = -1
    private var rPos: Int = -1

    private fun updateMidPoint(pos: Int) {
        val latLng0 = when (pos) {
            0 -> {
                lPos = mList.size - 1
                mList[mList.size - 2]
            }
            1 -> {
                lPos = 0
                mList[mList.size - 1]
            }
            else -> {
                lPos = pos - 1
                mList[pos - 2]
            }
        }
        val latLng1 = when (pos) {
            mList.size - 1 -> {
                rPos = 0
                mList[1]
            }
            mList.size - 2 -> {
                rPos = mList.size - 1
                mList[0]
            }
            else -> {
                rPos = pos + 1
                mList[pos + 2]
            }
        }
        val latL = getMidPoint(latLng0, mList[pos])
        val latR = getMidPoint(latLng1, mList[pos])
        mMarkerList[lPos].marker.position = latL
        mMarkerList[rPos].marker.position = latR
        mList[lPos] = latL
        mList[rPos] = latR
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
            pos = mList.size - 1
        }
    }

    private fun drawPoints() {
//        val bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_dot_prepare)
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

    private fun drawLineor() {
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
    }

    private var pLine: Polyline? = null
    private var pl: Polygon? = null


    /**
     * 定位SDK监听函数
     */
    inner class MyLocationListenner : BDLocationListener {

        override fun onReceiveLocation(location: BDLocation?) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }

            val locData = MyLocationData.Builder()
                    .accuracy(location.radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100f).latitude(location.latitude)
                    .longitude(location.longitude).build()
            mBaiduMap!!.setMyLocationData(locData)
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(
                        location.latitude,
                        location.longitude
                )
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(17.0f)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
        }

    }

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
            mLocClient != null -> // 退出时销毁定位
                mLocClient!!.stop()
            mBaiduMap != null -> // 关闭定位图层
                mBaiduMap!!.isMyLocationEnabled = false
            mMapView != null -> mMapView!!.onDestroy()
        }
    }
}