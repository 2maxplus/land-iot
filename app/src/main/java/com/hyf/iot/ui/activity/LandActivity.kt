package com.hyf.iot.ui.activity

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import com.hyf.iot.R
import com.hyf.iot.common.activity.BaseActivity
import com.hyf.iot.utils.mapCeshi.CombinationOverlay
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.layout_common_title.*
import java.util.*

class LandActivity : BaseActivity() {
    /**
     * MapView 是地图主控件
     */
    private var mBaiduMap: BaiduMap? = null
    private var combinationOverlayList: MutableList<CombinationOverlay>? = null
    private var list: MutableList<LatLng>? = null

    private var infoWindow: InfoWindow? = null
    private var tempOverlay: CombinationOverlay? = null// 当前选中的覆盖物
    private var lastx: Float = 0.toFloat()
    private var lasty: Float = 0.toFloat()
    private var offsetx: Float = 0.toFloat()
    private var offsety: Float = 0.toFloat()

    private var isDrag = false  //marker是否正在拖拽
    private var popupView: View? = null  //弹出View
    private var popup_btn_left: Button? = null
    private var popup_tv_name: TextView? = null
    private var popup_btn_right: Button? = null
    private val isPopviewShow = false

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun getLayoutId() = R.layout.activity_map

    override fun initView() {
        super.initView()
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.add_massif)
        mBaiduMap = mMapView!!.map
        val msu = MapStatusUpdateFactory.zoomTo(14.0f)
        mBaiduMap!!.setMapStatus(msu)
        combinationOverlayList = ArrayList()
        initOverlay()
    }


    fun initOverlay() {
        val llA = LatLng(39.963175, 116.400244)
        val llB = LatLng(39.942821, 116.369199)
        val llC = LatLng(39.939723, 116.425541)
        list = ArrayList()
        list!!.add(llA)
        list!!.add(llB)
        list!!.add(llC)

        val combinationOverlay = CombinationOverlay(mMapView!!)  // list
        combinationOverlayList!!.add(combinationOverlay)

        val southwest = LatLng(39.92235, 116.380338)
        val northeast = LatLng(39.947246, 116.414977)
        val bounds = LatLngBounds.Builder().include(northeast)
                .include(southwest).build()

        val u = MapStatusUpdateFactory
                .newLatLng(bounds.center)
        mBaiduMap!!.setMapStatus(u)

//        mBaiduMap!!.setOnMapTouchListener { motionEvent ->
//            when(motionEvent.action){
//                MotionEvent.ACTION_DOWN -> {
//
//                }
//                MotionEvent.ACTION_MOVE -> {
//
//                }
//                MotionEvent.ACTION_UP -> {
//
//                }
//            }
//            if (motionEvent.action == MotionEvent.ACTION_DOWN) {   //按下的时候 做处理
//                tempOverlay = null
//                lastx = motionEvent.x
//                lasty = motionEvent.y
//                val point = Point(motionEvent.x.toInt(), motionEvent.y.toInt())
//                val latlng = mBaiduMap!!.projection.fromScreenLocation(point)
//                //                    MapStatus.Builder builder = new MapStatus.Builder();
//
//                for (i in combinationOverlayList!!.indices) {
//                    val list = combinationOverlayList!![i].getLatLngList()
//                    if (SpatialRelationUtil.isPolygonContainsPoint(list, latlng)) {   //判断是否在多边形里面
//                        tempOverlay = combinationOverlayList!![i]
//                        mBaiduMap!!.uiSettings.isScrollGesturesEnabled = false
//                        mBaiduMap!!.hideInfoWindow()
//                        break
//                    }
//                }
//
//            } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {
//                if (tempOverlay != null) {
//                    //全部根据手指的移动将其转化成百度坐标
//                    if (!isDrag) {
//                        offsetx = motionEvent.x - lastx
//                        offsety = motionEvent.y - lasty
//                        lastx = motionEvent.x
//                        lasty = motionEvent.y
//                        tempOverlay!!.updateOverlayByPolygon(offsetx, offsety)
//                    }
//
//                }
//            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
//                if (tempOverlay != null) {
//                    mBaiduMap!!.uiSettings.isScrollGesturesEnabled = true
//                }
//            }
//        }

        mBaiduMap!!.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
            }

            override fun onMapPoiClick(mapPoi: MapPoi): Boolean {
                return false
            }
        })
        //点击事件
        mBaiduMap!!.setOnMarkerClickListener { marker -> updateMarkerClick(marker) }
        //必须长按才可以拖曳
        mBaiduMap!!.setOnMarkerDragListener(object : BaiduMap.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                updateMarkerDrag(marker)
            }

            override fun onMarkerDragEnd(marker: Marker) {
                isDrag = false
                mBaiduMap!!.uiSettings.isScrollGesturesEnabled = true
            }

            override fun onMarkerDragStart(marker: Marker) {
                isDrag = true
                mBaiduMap!!.uiSettings.isScrollGesturesEnabled = false
            }
        })
        mBaiduMap!!.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                mBaiduMap!!.hideInfoWindow()
            }

            override fun onMapPoiClick(mapPoi: MapPoi): Boolean {
                return false
            }
        })
        mBaiduMap!!.setOnPolylineClickListener { polyline ->
            mBaiduMap!!.hideInfoWindow()
            updateLineClick(polyline)
        }
    }



    /**
     * 更新marker点击
     *
     * @param marker
     * @return
     */
    private fun updateMarkerClick(marker: Marker): Boolean {
        for (i in combinationOverlayList!!.indices) {
            if (combinationOverlayList!![i].getMarkerList()!!.contains(marker)) {
                val button = Button(applicationContext)
                button.setBackgroundResource(R.drawable.custom_info_bubble)
                val ll = marker.position
                button.text = "删除当前点"
                button.setTextColor(Color.BLACK)
                button.setOnClickListener { combinationOverlayList!![i].updateOverlayByRemoveOneMarker(marker) }
                infoWindow = InfoWindow(button, ll, -27)

                mBaiduMap!!.showInfoWindow(infoWindow)
                return true
            }
        }
        return false
    }

    /**
     * 更新线段点击
     *
     * @param polyline
     * @return
     */
    private fun updateLineClick(polyline: Polyline): Boolean {
        for (i in combinationOverlayList!!.indices) {
            if (combinationOverlayList!![i].getPolylineList()!!.contains(polyline)) {
                combinationOverlayList!![i].updateOverlayByLineClick(polyline)
                return true
            }
        }
        return false
    }

    private fun updateMarkerDrag(marker: Marker) {
        for (i in combinationOverlayList!!.indices) {
            if (combinationOverlayList!![i].getMarkerList()!!.contains(marker)) {
                combinationOverlayList!![i].updateOverlayByMarker(marker)
            }
        }

    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    fun clearOverlay(view: View?) {
        mBaiduMap!!.clear()
    }

    /**
     * 重新添加Overlay
     *
     * @param view
     */
    fun resetOverlay(view: View) {
        clearOverlay(null)
        initOverlay()
    }


    /**
     * 创建popupView
     *
     * @param title
     * @param left
     * @param right
     */
    private fun createPopupView(title: String, left: View.OnClickListener, right: View.OnClickListener) {
        if (popupView == null) {
//            popupView = LayoutInflater.from(this).inflate(R.layout.listformat_popview_new, null)
//            popup_btn_left = popupView!!.findViewById<View>(R.id.popup_btn_left) as Button
//            popup_btn_right = popupView!!.findViewById<View>(R.id.popup_btn_right) as Button
//            popup_tv_name = popupView!!.findViewById<View>(R.id.popup_tv_name) as TextView
            popup_btn_left!!.setOnClickListener(left)

            popup_btn_right!!.setOnClickListener(right)

        }
        popup_btn_left!!.text = title
        popup_btn_left!!.setOnClickListener(left)
        popup_btn_right!!.setOnClickListener(right)
    }


    override fun onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView!!.onPause()
        super.onPause()
    }

    override fun onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView!!.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView!!.onDestroy()
        super.onDestroy()


    }

}