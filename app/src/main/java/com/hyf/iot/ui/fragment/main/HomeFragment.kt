package com.hyf.iot.ui.fragment.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hyf.iot.R
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.DeviceCoordinatesContract
import com.hyf.iot.domain.DeviceCoordinates
import com.hyf.iot.presenter.DeviceCoordinatesPresenter
import com.hyf.iot.ui.activity.*
import com.hyf.iot.utils.UIUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.spinner.SpinnerChooseAdapter
import com.hyf.iot.widget.spinner.SpinnerUtils
import java.util.*
import kotlinx.android.synthetic.main.fragment_home.*
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapStatusUpdate
import com.hyf.iot.common.Constant
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.domain.farm.Farm


class HomeFragment : BaseMvpFragment<DeviceCoordinatesContract.IPresenter>(), DeviceCoordinatesContract.IView {

    //百度
    private var mBaiduMap: BaiduMap? = null
    private var ooA: MarkerOptions? = null
    private var mLocationClient: LocationClient? = null
    /**当前地理坐标 */
    private var mLoactionLatLng: LatLng? = null
    internal var isFirstLoc = true // 是否首次定位
    private val markerlist = ArrayList<Marker>()
    private val BAIDU_READ_PHONE_STATE = 100
    private var heatmap: HeatMap? = null

    private var isDestroy: Boolean = false
    private var locationLat: Double = 0.0
    private var locationLng: Double = 0.0

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
        for (item in data) {
            initmaker(LatLng(item.latitude, item.longitude), "0", 0, item)
        }
    }

    override fun errorPage(msg: String?) {
        if (msg.isNullOrEmpty()) {
            context?.showToast(R.string.net_error)
        } else {
            context?.showToast(msg)
        }
    }

    override fun initView() {
        mapAction()
        floating_menu.hideMenuButton(false)
        floating_menu.setClosedOnTouchOutside(true)
        mUiHandler.postDelayed({ floating_menu.showMenuButton(true) }, 400)
        fab_btn_switch_farm.setOnClickListener {
            floating_menu.close(true)
            val intent = Intent(context, FarmListActivity::class.java)
            startActivityForResult(intent, ON_SUCCESS)
        }
        fab_btn_add_farm.setOnClickListener {
            floating_menu.close(true)
            activity?.newIntent<FarmAddOrEditActivity>()
        }
        fab_btn_add_land.setOnClickListener {
            floating_menu.close(true)
            activity?.newIntent<MassifActivity>()
        }
        //重定位
        iv_location.setOnClickListener {
            Log.e("isStarted==", ":" + mLocationClient!!.isStarted)
            requestLocation()
        }
    }

    private fun mapAction() {
        mMapView.showZoomControls(true)
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(12f))
        mData.add("土壤温度")
        mData.add("土壤湿度")
        mData.add("日照强度")
        mData.add("空气温度")
        mData.add("空气湿度")
        tvMapHead.setOnClickListener {
            SpinnerUtils(activity!!,
                    tvMapHead,
                    mData,
                    object : SpinnerChooseAdapter.MyItemClickListener {
                        override fun onClick(view: View) {
                            selectPosition = view.tag as Int
                            tvMapHead.text = mData[selectPosition]
                            addHeatMap()
                        }
                    }, selectPosition).showPopupWindow()
        }
        initMap() //init为定位方法
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
                for (i in 0..800) {
                    if (locationLat > 0 && locationLng > 0) {
                        data.add(getRandomLatLng(LatLng(locationLat, locationLng), 0.60))
                    }
                }
                if (data.size > 0 && data.isNotEmpty()) {
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
    private fun setBaiduHeatMap(isShow: Boolean) {
        mBaiduMap!!.isBaiduHeatMapEnabled = isShow
    }


    @Suppress("DEPRECATION")
    private fun initMap() {
        mBaiduMap!!.setOnMapLoadedCallback {
            mMapView.setZoomControlsPosition(Point(20, UIUtils.getScreenHeight(context!!) / 2))
        }
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        // 定位初始化
        mLocationClient = LocationClient(context)
        mLocationClient!!.registerLocationListener(MyLocationListener())
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(0)
        mLocationClient!!.locOption = option
        mLocationClient!!.start()
        //初始化缩放比例
        val msu = MapStatusUpdateFactory.zoomTo(17.0f)
        mBaiduMap!!.setMapStatus(msu)
        mBaiduMap!!.setOnMapTouchListener(MyMapTouchListener())
        mBaiduMap!!.setOnMarkerClickListener { marker ->
            val bundleMarker = marker!!.extraInfo
            val type = bundleMarker.getInt("type")
            when(type){
                1 -> {  //设备信息
                    val item = bundleMarker.getParcelable<DeviceCoordinates>("item")
                    val bundle = Bundle()
                    bundle.putString("id", item.id)  //设备ID
                    activity?.newIntent<ValveDetailActivity>(bundle)
                }
                2 -> {  //农场信息
                    val item = bundleMarker.getParcelable<Farm>("item")
                    val latLng = LatLng(item.latitude, item.longitude)
                    val view = LayoutInflater.from(context).inflate(R.layout.item_popwindow, null)
                    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                    val tvAddress = view.findViewById<TextView>(R.id.tv_address)
                    val tvLinkman = view.findViewById<TextView>(R.id.tv_linkman)
                    val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
                    tvTitle.text = item.name
                    tvAddress.text = item.address
                    tvLinkman.text = item.linkMan
                    tvPhone.text = item.linkPhone
                    val offset = UIUtils.dip2px(activity!!, -25f)
                    view.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString(Constant.KEY_PARAM_ID, item.id)
                        activity?.newIntent<FarmDetailActivity>(ON_SUCCESS,bundle)
                        mBaiduMap!!.hideInfoWindow()
                    }
                    val mInfoWindow = InfoWindow(view, latLng, offset)
                    mBaiduMap!!.showInfoWindow(mInfoWindow)
                }
            }

            true
        }
    }


    private fun initmaker(point: LatLng, status: String, cover_type_id: Int, item: DeviceCoordinates) {
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
        val mBundle = Bundle()
        mBundle.putInt("type",1)
        mBundle.putParcelable("item", item)
        mMarkerA.extraInfo = mBundle
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
            mLocationClient != null -> // 退出时销毁定位
                mLocationClient!!.stop()
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
            val mLantitude = location.latitude
            val mLongtitude = location.longitude
            mLoactionLatLng = LatLng(mLantitude, mLongtitude)
            if (isFirstLoc) {
                isFirstLoc = false
                locationLat = location.latitude
                locationLng = location.longitude
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
            }
//            addHeatMap()
        }

        fun onReceivePoi(poiLocation: BDLocation) {

        }
    }

    fun getRandomLatLng(point: LatLng, circle: Double): LatLng {
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


    /**
     * 手动请求定位的方法
     */
    fun requestLocation() {
        if (mLocationClient != null && mLocationClient!!.isStarted) {
            mLocationClient!!.requestLocation()
            // 实现动画跳转
            mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
        }
    }


    private fun initMarker(point: LatLng, data: Farm) {
        val bdA: BitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_farm_loc)
        val ooA = MarkerOptions().position(point).icon(bdA)
        val mMarkerA = mBaiduMap!!.addOverlay(ooA)
        val mBundle = Bundle()
        mBundle.putInt("type",2)
        mBundle.putParcelable("item", data)
        mMarkerA.extraInfo = mBundle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ON_SUCCESS -> {
                    val farm = data!!.getParcelableExtra<Farm>(Constant.KEY_PARAM_1)
                    val mLatLng = LatLng(farm.latitude, farm.longitude)
                    mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng))
                    initMarker(mLatLng, farm)
                }
            }
        }
    }

}


