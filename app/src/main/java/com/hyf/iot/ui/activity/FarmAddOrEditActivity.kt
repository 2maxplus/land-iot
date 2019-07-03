package com.hyf.iot.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.baidu.mapsdkplatform.comapi.location.CoordinateType
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.Constant.KEY_PARAM_1
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.FarmContract
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.presenter.FarmAddOrEditPresenter
import com.hyf.iot.utils.StringUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.dialog.MyDialog
import kotlinx.android.synthetic.main.activity_add_farm.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * 农场
 *
 * */
class FarmAddOrEditActivity : BaseMvpActivity<FarmContract.IPresenter>(), FarmContract.IView {

    private var mBaiduMap: BaiduMap? = null
    /**当前物理坐标 */
    private var mCenterPoint: Point? = null
    /**当前地理坐标 */
    private var mLoactionLatLng: LatLng? = null
    internal var mLatitude = 30.654008
    internal var mLongtitude = 104.093591
    private var mAddr = ""
    private var mFarmName = ""
    private var mLinkMan = ""
    private var mLinkPhone = ""
    private var mProvince = ""
    private var mCity = ""
    private var mDistrict = ""
    /**是否第一次定位   */
    var isFirstLoc = true
    /**地理编码 */
    private var mGeoCoder: GeoCoder? = null
    private var mLocationClient: LocationClient? = null
    private var mImageViewPointer: ImageView? = null
    private var addressDetail: ReverseGeoCodeResult.AddressComponent? = null
    // 农场ID
    private var id = ""
    private lateinit var dialogs: MyDialog

    override fun getLayoutId() = R.layout.activity_add_farm

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            val data = bundle.getParcelable<Farm>(KEY_PARAM_1)
            id = data.id!!
            mLatitude = data.latitude
            mLongtitude = data.longitude
            mAddr = data.address!!
            mFarmName = data.name!!
            mLinkMan = data.linkMan!!
            mLinkPhone = data.linkPhone!!
        }
    }

    override fun initView() {
        super.initView()
        iv_back.setOnClickListener { onBackPressed() }
        tv_location.text = "移动地图获取位置"
        if (id.isNullOrEmpty()) {
            tv_title.text = getString(R.string.add_farm)
            btn_add_farm.text = getString(R.string.create_completion)
        } else {
            tv_title.text = getString(R.string.edit_farm)
            btn_add_farm.text = getString(R.string.update_completion)
            tv_location.text = "移动地图获取位置"
            et_address_detail.setText(mAddr)
            et_farm_name.setText(mFarmName)
            et_linkMan.setText(mLinkMan)
            et_linkPhone.setText(mLinkPhone)
        }

        initSettingMapView()
        btn_add_farm.setOnClickListener {
            mFarmName = et_farm_name.text.toString()
            mAddr = et_address_detail.text.toString()
            if (mFarmName.isNullOrBlank()) {
                et_farm_name.requestFocus()
                showToast("请输入农场名字")
                return@setOnClickListener
            }
            mLinkMan = et_linkMan.text.toString()
            if (mLinkMan.isNullOrBlank()) {
                et_linkMan.requestFocus()
                showToast("请输入农场联系人")
                return@setOnClickListener
            }
            mLinkPhone = et_linkPhone.text.toString()
            if (mLinkPhone.isNullOrBlank()) {
                et_linkPhone.requestFocus()
                showToast("请输入联系电话")
                return@setOnClickListener
            }
            if (!StringUtils.isPhone(mLinkPhone)) {
                showToast("请输入正确的手机号")
                et_linkPhone.requestFocus()
                return@setOnClickListener
            }
            if (id.isNullOrEmpty())
                getPresenter().farmAdd(mFarmName, mAddr, mLinkMan,mLinkPhone,mLatitude, mLongtitude,  mProvince, mCity,mDistrict)
            else
                getPresenter().farmEdit(mFarmName, mAddr, mLinkMan,mLinkPhone,mLatitude, mLongtitude, mProvince, mCity,mDistrict, id)
        }
    }

    override fun registerPresenter() = FarmAddOrEditPresenter::class.java

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
        dialogs = MyDialog(this, "添加成功,要继续添加地块吗？", View.OnClickListener {
            when (it.id) {
                R.id.left_text -> {  //
                    setResult(RESULT_OK)
                    finish()
                    dialogs.dismiss()
                }
                R.id.right_text -> {
                    //继续
                    newIntent<MassifActivity>(ON_SUCCESS)
                    dialogs.dismiss()
                }
            }
        })
        dialogs.show()

    }

    override fun editSuccess() {
        showToast("编辑完成")
        setResult(RESULT_OK)
        finish()
    }

    private fun initSettingMapView() {
        mBaiduMap = mMapView.map
        // 初始化地图
        mMapView.showZoomControls(true)
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true
        initLoc()
        //初始化缩放比例
        val msu = MapStatusUpdateFactory.zoomTo(17.0f)
        mBaiduMap!!.setMapStatus(msu)
        mBaiduMap!!.setOnMapTouchListener(MyMapTouchListener())

        // 初始化当前MapView中心屏幕坐标(物理坐标)
        mMapView.post {
            mCenterPoint = mBaiduMap!!.mapStatus.targetScreen
            val x = mCenterPoint!!.x
            val y = mCenterPoint!!.y

            mImageViewPointer = ImageView(applicationContext)
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_location_chatbox)
            mImageViewPointer!!.setImageBitmap(bitmap)
            mImageViewPointer!!.x = (x - bitmap.width / 2).toFloat()
            mImageViewPointer!!.y = (y - bitmap.height).toFloat()
            val parent = mMapView.parent as ViewGroup
            parent.addView(mImageViewPointer, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT))
        }
        //初始化当前地理坐标
        mLoactionLatLng = mBaiduMap!!.mapStatus.target

        // 地理编码
        mGeoCoder = GeoCoder.newInstance()
        mGeoCoder!!.setOnGetGeoCodeResultListener(MyGetGeoCoderResultListener())

    }

    private fun initLoc() {
        // 定位初始化
        mLocationClient = LocationClient(this)
        mLocationClient!!.registerLocationListener(MyBDLocationListener())
        val option = LocationClientOption()
        option.isOpenGps = true// 打开gps
        option.setCoorType(CoordinateType.BD09LL) // 设置坐标类型
        option.setScanSpan(0)
        mLocationClient!!.locOption = option
        mLocationClient!!.start()
    }

    // 定位监听器
    inner class MyBDLocationListener : BDLocationListener {

        override fun onReceiveLocation(location: BDLocation?) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return
            }
            val data = MyLocationData.Builder()//
                    .accuracy(location.radius)
                    // .direction(mCurrentX)//
                    .direction(location.direction)
                    .latitude(location.latitude)//
                    .longitude(location.longitude)//
                    .build()
            mBaiduMap!!.setMyLocationData(data)
            if(id.isNullOrEmpty()){
                mLatitude = location.latitude
                mLongtitude = location.longitude
                mLoactionLatLng = LatLng(mLatitude, mLongtitude)
                // 是否第一次定位
                if (isFirstLoc) {
                    isFirstLoc = false
                    // 实现动画跳转
                    mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
                    mGeoCoder!!.reverseGeoCode(ReverseGeoCodeOption().location(mLoactionLatLng))
                }
            }else{
                mLoactionLatLng = LatLng(mLatitude, mLongtitude)
                mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
            }
        }
    }

    // 地图触摸事件监听器
    private inner class MyMapTouchListener : BaiduMap.OnMapTouchListener {

        override fun onTouch(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_UP) {
                if (mCenterPoint == null) {
                    return
                }
                // 获取当前MapView中心屏幕坐标对应的地理坐标
                val currentLatLng = mBaiduMap!!.projection.fromScreenLocation(mCenterPoint)
                //发起反地理编码检索
                mGeoCoder!!.reverseGeoCode(ReverseGeoCodeOption().location(currentLatLng))
//                mProgressBar.setVisibility(View.VISIBLE)
            }
        }
    }

    /**
     * 手动请求定位的方法
     */
    fun requestLocation() {
        //        isRequest = true;

        if (mLocationClient != null && mLocationClient!!.isStarted) {
            mLocationClient!!.requestLocation()
            // 实现动画跳转
            mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLoactionLatLng))
            mGeoCoder!!.reverseGeoCode(ReverseGeoCodeOption().location(mLoactionLatLng))
        }
    }

    // 地理编码监听器
    private inner class MyGetGeoCoderResultListener : OnGetGeoCoderResultListener {

        @SuppressLint("SetTextI18n")
        override fun onGetReverseGeoCodeResult(result: ReverseGeoCodeResult?) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
                showToast("没有检索到地址")
            } else {
                // 当前位置信息
                val mCurentInfo = PoiInfo()
                mCurentInfo.address = result.address
                mCurentInfo.location = result.location
                val mAddr = mCurentInfo.address
                addressDetail = result.addressDetail
                mProvince = addressDetail?.province!!
                mCity = addressDetail?.city!!
                mDistrict = addressDetail?.district!!
                tv_location.text = mProvince + mCity + mDistrict
                et_address_detail.setText(mAddr)
                //经纬度
                mLatitude = result.location.latitude
                mLongtitude = result.location.longitude

                mBaiduMap!!.clear()
//                mProgressBar.setVisibility(View.INVISIBLE)
                runShakeAnimation(mImageViewPointer!!)

            }// 获取反向地理编码结果
        }

        override fun onGetGeoCodeResult(result: GeoCodeResult?) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
            }
            // 获取地理编码结果
        }
    }

    fun runShakeAnimation(view: View) {
        val anim = TranslateAnimation(0f, 0f, 0f, 10f)
        anim.duration = 500
        anim.interpolator = CycleInterpolator(1f)
        view.startAnimation(anim)
    }


    override fun onDestroy() {
        when {
            mLocationClient != null -> // 退出时销毁定位
            {
                mLocationClient!!.stop()
                // 关闭定位图层
                mBaiduMap!!.isMyLocationEnabled = false
            }
            mBaiduMap != null -> // 关闭定位图层
                mBaiduMap!!.isMyLocationEnabled = false
            mMapView != null -> mMapView!!.onDestroy()
            mGeoCoder != null -> mGeoCoder!!.destroy()
        }
        super.onDestroy()
    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ON_SUCCESS -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}