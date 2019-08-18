package com.hyf.iot.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.adapter.farm.MassifAdapter
import com.hyf.iot.common.Constant.KEY_PARAM_1
import com.hyf.iot.common.Constant.KEY_PARAM_ID
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.FarmDetailContract
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.domain.farm.Massif
import com.hyf.iot.presenter.FarmDetailPresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.RecycleViewDivider
import com.hyf.iot.widget.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_farm_detail.*
import kotlinx.android.synthetic.main.farm_item.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*

/**
 * 农场详情
 *
 * */
class FarmDetailActivity : BaseMvpActivity<FarmDetailContract.IPresenter>(), FarmDetailContract.IView {

    private var mBaiduMap: BaiduMap? = null
    private var id: String = ""
    private var data: Farm? = null
    private val mLoadingDialog by lazy { LoadingDialog(this) }
    private val mMassifAdapter by lazy { MassifAdapter(this, ArrayList()) }

    override fun getLayoutId() = R.layout.activity_farm_detail

    override fun registerPresenter() = FarmDetailPresenter::class.java

    override fun showFarmDetail(data: Farm) {
        this.data = data
        tv_farm_name.text = data.name
        tv_farm_address.text = data.address
        tv_farm_linkman.text = data.linkMan
        tv_farm_linkphone.text = data.linkPhone
        val mLatLng = LatLng(data.latitude, data.longitude)
        mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng))
        initMarker(mLatLng)
        getPresenter().getMassifList(data.id!!)
    }

    private fun initMarker(point: LatLng) {
        val bdA: BitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_farm_loc)
        val ooA = MarkerOptions().position(point).icon(bdA)
//        val mMarkerA =
        mBaiduMap!!.addOverlay(ooA)
//        val mBundle = Bundle()
//        mBundle.putParcelable("item", data)
//        mMarkerA.extraInfo = mBundle
    }

    override fun showMassifList(data: MutableList<Massif>) {
        mMassifAdapter.mData.clear()
        mMassifAdapter.mData.addAll(data)
        mMassifAdapter.notifyDataSetChanged()
    }

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.farm_detail)
        tv_operate.visibility = View.VISIBLE
        tv_operate.text = "编辑"
        tv_operate.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(KEY_PARAM_1, data)
            newIntent<FarmAddOrEditActivity>(ON_SUCCESS, bundle)
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FarmDetailActivity, LinearLayout.VERTICAL, false)
//            addItemDecoration(ItemDecoration(2))
            addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL))
            mMassifAdapter.isShowLoadMore(false)
            adapter = mMassifAdapter
        }
        //地图初始化
        mBaiduMap = mMapView!!.map
        mBaiduMap!!.setMapStatus(MapStatusUpdateFactory.zoomTo(15f))
    }


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            id = bundle.getString(KEY_PARAM_ID)
        }
    }

    override fun initData() {
        getPresenter().getFarmDetail(id)
    }

    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun onError(errorMsg: String?) {
        mLoadingDialog.dismiss()
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == ON_SUCCESS) {
                initData()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

}