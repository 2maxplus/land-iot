package com.hyf.iot.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.hyf.iot.R
import com.ljb.kt.utils.NetLog


class MapLocationUtil(private val context: Context) {
    protected var tempMode: LocationClientOption.LocationMode = LocationClientOption.LocationMode.Device_Sensors // GPS定位
    protected var locationClient: LocationClient? = null
    private var option: LocationClientOption? = null

    init {
        locationClient = LocationClient(context.applicationContext)
        initLocationOption(context)
    }

    fun getLocalPoint(context: Context, handler: Handler) {//获取当前位置
        locationClient!!.registerLocationListener { location ->
            val message = Message()
            message.what = MAPLOCAL_SUCCESS
            message.obj = location
            handler.sendMessage(message)
            locationClient!!.stop()
        }
        locationClient!!.start()
    }

    private fun initLocationOption(context: Context) {
        option = LocationClientOption()
        if (NetUtils.checkHasNet(context)) {
            //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option!!.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        } else {
            option!!.locationMode = LocationClientOption.LocationMode.Device_Sensors
        }
        option!!.setCoorType("bd09ll")//返回百度经纬度坐标——ll是英文字母l
        option!!.setNeedDeviceDirect(true)//需要手机的方向信息
        option!!.setScanSpan(2000)//设置定位次数，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option!!.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
        option!!.isOpenGps = true//可选，默认false,设置是否使用gps
        option!!.setTimeOut(9 * 1000)//10秒定位不到就返回
        option!!.isLocationNotify = true//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option!!.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option!!.setIsNeedLocationPoiList(true)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option!!.setIgnoreKillProcess(true)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option!!.SetIgnoreCacheException(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option!!.setEnableSimulateGps(false)
        locationClient!!.locOption = option
    }

    fun setLocalPoint(handler: Handler, baiduMap: BaiduMap, isNeedShowLocation: Boolean) {//获取当前位置
        if (locationClient == null) {
            locationClient = LocationClient(context.applicationContext)
            initLocationOption(context)
        }
        locationClient!!.registerLocationListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            val message = Message()
            message.what = MAPLOCAL_SUCCESS
            message.obj = location
            NetLog.d("定位成功:" + location.latitude + ":" + location.longitude + "\t" + location.addrStr)
            handler.sendMessage(message)
            if (isNeedShowLocation) {
                setLocationMark(baiduMap, latLng)//设置定位图标
            }
            getAroundPoi(handler, LatLng(location.latitude, location.longitude))
        }
        locationClient!!.start()
    }


    // 重新定位
    fun restartLocation() {
        if (locationClient != null) {
            if (locationClient!!.isStarted) {
                locationClient!!.stop()
            }
            if (option != null)
                if (NetUtils.checkHasNet(context)) {
                    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
                    option!!.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
                } else {
                    option!!.locationMode = LocationClientOption.LocationMode.Device_Sensors
                }
            locationClient!!.locOption = option
            locationClient!!.start()
        }
    }

    //设置定位点
    fun setLocationMark(baiduMap: BaiduMap?, latLng: LatLng) {
        if (baiduMap == null) return
        baiduMap.clear()//清除之前的marker
        //构建Marker图标
        val bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_farm_loc)
        //构建MarkerOption，用于在地图上添加Marker
        val option = MarkerOptions()
                .position(latLng)
                .icon(bitmap)
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option)
        //设置显示中心点
        val mapStatus = MapStatus.Builder(baiduMap.mapStatus)
                .target(latLng)//设置中心点
                .zoom(17f)//缩放比例
                .overlook(-10f)//俯视角度
                .build()
        val statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus)
        baiduMap.setMapStatus(statusUpdate)//设置地图状态改变
    }

    /**
     * 设置定位点(不清除之前的点)
     *
     * @param baiduMap
     * @param latLng
     * @param zoom     设置地图放大比例
     */
    fun setLocationMark(baiduMap: BaiduMap?, latLng: LatLng, zoom: Float) {
        if (baiduMap == null) return
        //构建Marker图标
        val bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_farm_loc)
        //构建MarkerOption，用于在地图上添加Marker
        val option = MarkerOptions()
                .position(latLng)
                .icon(bitmap)
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option)
        //设置显示中心点
        val mapStatus = MapStatus.Builder(baiduMap.mapStatus)
                .target(latLng)//设置中心点
                .zoom(zoom)//缩放比例
                .overlook(-10f)//俯视角度
                .build()
        val statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus)
        baiduMap.setMapStatus(statusUpdate)//设置地图状态改变
    }

    //设置定位点_图标为百度系统自带

    /**
     * @param baiduMap
     * @param latLng
     * @param zoom     地图放大比例
     */
    fun setLocation(baiduMap: BaiduMap, latLng: LatLng, zoom: Float) {
        baiduMap.clear()
        // 开启定位图层
        baiduMap.isMyLocationEnabled = true
        // 构造定位数据
        val locData = MyLocationData.Builder()
                .accuracy(0f)
                .direction(0f)// 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(latLng.latitude)
                .longitude(latLng.longitude).build()
        // 设置定位数据
        baiduMap.setMyLocationData(locData)
        val mapStatus = MapStatus.Builder(baiduMap.mapStatus)
                .target(latLng)//设置中心点
                .zoom(zoom)//缩放比例
                .overlook(-10f)//俯视角度
                .build()
        val statusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus)
        baiduMap.setMapStatus(statusUpdate)//设置地图状态改变
    }

    fun getAroundPoi(handler: Handler, latLng: LatLng) {
        // 创建GeoCoder实例对象
        val geoCoder = GeoCoder.newInstance()
        // 设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(result: GeoCodeResult?) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
            }

            override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    return
                }
                val poiInfos = result.poiList
                if (poiInfos != null && poiInfos.size > 0) {
                    val message = Message()
                    message.what = MAPLOCAL_SEARCH_OK
                    message.obj = poiInfos
                    handler.sendMessage(message)
                    geoCoder.destroy()
                    stopLocation()
                } else {
                    val message = Message()
                    message.what = MAPLOCAL_SEARCH_FAILURE
                    message.obj = poiInfos
                    handler.sendMessage(message)
                }
            }
        })
        //发起地理编码检索
        geoCoder.reverseGeoCode(ReverseGeoCodeOption().location(latLng))
    }

    //关闭定位
    fun stopLocation() {
        if (locationClient != null && locationClient!!.isStarted) {
            locationClient!!.stop()
        }
    }

    //是否开启GPS
    fun isOpenGPS(activity: Activity): Boolean {
        val locManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 设置GPS弹框
     *
     * @param activity
     * @param GPS_REQUEST_CODE
     */
    fun showSetGPSDialog(activity: Activity, GPS_REQUEST_CODE: Int) {
        AlertDialog.Builder(activity)
                .setTitle("定位提示")
                .setMessage("GPS未开启，GPS开启后定位更精确，是否设置GPS？")
                // 拒绝, 退出应用
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.setting
                ) { dialog, which ->
                    //跳转GPS设置界面
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    activity.startActivityForResult(intent, GPS_REQUEST_CODE)
                }

                .setCancelable(false)
                .show()
    }

    companion object {
        val MAPLOCAL_SUCCESS = 11
        val MAPLOCAL_FAILURE = 22
        val MAPLOCAL_SEARCH_OK = 33
        val MAPLOCAL_SEARCH_FAILURE = 44
    }
}