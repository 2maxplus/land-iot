package com.hyf.iot.ui.fragment.main

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hyf.iot.R
import com.hyf.iot.ui.activity.FarmAddOrEditActivity
import com.hyf.iot.ui.activity.LandActivity
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.DeviceCoordinatesContract
import com.hyf.iot.domain.DeviceCoordinates
import com.hyf.iot.presenter.DeviceCoordinatesPresenter
import com.hyf.iot.utils.UIUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.spinner.SpinnerChooseAdapter
import com.hyf.iot.widget.spinner.SpinnerUtils
import java.util.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment: BaseMvpFragment<DeviceCoordinatesContract.IPresenter>(),DeviceCoordinatesContract.IView {

    private val deviceList: ArrayList<DeviceCoordinates> = ArrayList()
    //百度
    private var mBaiduMap: BaiduMap? = null
    private var ooA: MarkerOptions? = null
    private var mLocClient: LocationClient? = null
    internal var isFirstLoc = true // 是否首次定位
    private val markerlist = ArrayList<Marker>()
    private val BAIDU_READ_PHONE_STATE = 100
    private var heatmap: HeatMap? = null

    private var isDestroy: Boolean = false
    private var locationLat : Double = 0.0
    private var locationLng : Double = 0.0

    private var mData = ArrayList<String>()
    private var selectPosition = 0

    private val mUiHandler = Handler()

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun registerPresenter() = DeviceCoordinatesPresenter::class.java

    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun showPage(data: MutableList<DeviceCoordinates>) {
        deviceList.addAll(data)
        for(latlng in data){
            initmaker( LatLng(latlng.latitude,latlng.longitude),"0",0)
        }

        mBaiduMap!!.setOnMarkerClickListener { marker ->
            for (i in markerlist.indices) {
                if (marker == markerlist[i]) {
                    val bundle = Bundle()
                    bundle.putString("id", deviceList[i].id)  //设备ID
                    activity?.newIntent<ValveDetailActivity>(bundle)
                }
            }
            true
        }
    }

    override fun errorPage(t: Throwable) {

    }

    override fun initView() {
        mapAction()
        floating_menu.hideMenuButton(false)
        floating_menu.setClosedOnTouchOutside(true)
        mUiHandler.postDelayed({ floating_menu.showMenuButton(true) }, 400)
        fab_btn_add_farm.setOnClickListener {
            floating_menu.close(true)
            activity?.newIntent<FarmAddOrEditActivity>()
        }
        fab_btn_add_land.setOnClickListener {
            floating_menu.close(true)
            activity?.newIntent<LandActivity>()
        }
    }

    private fun mapAction(){
        mMapView.showZoomControls(true)
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(12f))
        mData.add("土壤温度")
        mData.add("土壤湿度")
        mData.add("日照强度")
        mData.add("空气温度")
        mData.add("空气湿度")
        tvMapHead.setOnClickListener{
            SpinnerUtils(activity!!,
                    tvMapHead,
                    mData,
                    object : SpinnerChooseAdapter.MyItemClickListener {
                        override fun onClick(view: View) {
                            selectPosition = view.tag as Int
                            tvMapHead.text = mData[selectPosition]
                            addHeatMap()
                        }
                    },selectPosition).showPopupWindow()
        }
        initbaidu() //init为定位方法
    }

    override fun initData() {
        getPresenter().getCoordinates()
    }

    //热力图自定义数据
    private fun addHeatMap() {
        val h = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (!isDestroy) {
                    mBaiduMap!!.addHeatMap(heatmap)
                }
            }
        }
        object : Thread() {
            override fun run() {
                super.run()
                val data = ArrayList<LatLng>()
                //获取随机数据 半径0.6
                for (i in 0 .. 800) {
                    if(locationLat > 0 && locationLng > 0){
                        data.add(getRandomLatLng(LatLng(locationLat,locationLng),0.60))
                    }
                }
                if(data.size > 0 && data.isNotEmpty()){
                    heatmap = HeatMap.Builder().data(data).build()
                    h.sendEmptyMessage(0)
                }
            }
        }.start()
    }

    /**
     * 设置是否显示百度热力图
     *
     * @param isShow
     */
    private fun setBaiduHeatMap(isShow : Boolean) {
        mBaiduMap!!.isBaiduHeatMapEnabled = isShow
    }


    @Suppress("DEPRECATION")
    private fun initbaidu() {
        mBaiduMap!!.setOnMapLoadedCallback {
            mMapView.setZoomControlsPosition(Point(20, UIUtils.getScreenHeight(context!!)/2))
        }
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        // 定位初始化
        mLocClient = LocationClient(context)
        mLocClient!!.registerLocationListener(MyLocationListener())
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(0)
        mLocClient!!.locOption = option
        mLocClient!!.start()
        mBaiduMap!!.setOnMapTouchListener(MyMapTouchListener())

    }


    private fun initmaker(point: LatLng, status: String, cover_type_id: Int) {
        val bdA: BitmapDescriptor = when (cover_type_id) {
            0 -> when (status) {
                "0" -> BitmapDescriptorFactory
                    .fromResource(R.drawable.main_icon_device)
                "1" -> BitmapDescriptorFactory
                    .fromResource(R.drawable.main_icon_device_offline)
                else -> BitmapDescriptorFactory
                    .fromResource(R.drawable.main_icon_device)
            }
            else -> BitmapDescriptorFactory
                .fromResource(R.drawable.main_icon_device)
        }
        ooA = MarkerOptions().position(point).icon(bdA)
//            .zIndex(9)
            .draggable(true)
        val mMarkerA = mBaiduMap!!.addOverlay(ooA)
        markerlist.add(mMarkerA as Marker)
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(12f))
    }

    // 地图触摸事件监听器
    private inner class MyMapTouchListener : BaiduMap.OnMapTouchListener {

        override fun onTouch(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_UP) {

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
        isDestroy = true
         when {
             mLocClient != null -> // 退出时销毁定位
                 mLocClient!!.stop()
             mBaiduMap != null -> // 关闭定位图层
                 mBaiduMap!!.isMyLocationEnabled = false
             mMapView != null -> mMapView!!.onDestroy()
         }
    }

    /**
     * 定位SDK监听函数
     */
    inner class MyLocationListener : BDLocationListener {

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
            //     Log.e("TAG", "lat_lng----->" +location.getLatitude()+"--------"+location.getLongitude());
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(
                    location.latitude,
                    location.longitude
                )
                locationLat = location.latitude
                locationLng = location.longitude
                val builder = MapStatus.Builder()
                builder.target(ll).zoom(14.0f)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
            }
//            for (i in 0..49) {
//                initmaker( getRandomLatLng(LatLng(locationLat,locationLng),0.25),"0",0)
//            }
//            addHeatMap()
        }

        fun onReceivePoi(poiLocation: BDLocation) {

        }
    }

    fun getRandomLatLng(point: LatLng,circle: Double): LatLng {
        val random = Random()
        val cirx = point.latitude
        val ciry = point.longitude
        val circleR = circle

        val r = Math.sqrt(Math.random()) * circleR
        val theta = random.nextInt(3600000) * 0.0001

        val x = r * Math.sin(theta)
        val y = r * Math.cos(theta)

        return LatLng(cirx + x, ciry + y)
    }


}


