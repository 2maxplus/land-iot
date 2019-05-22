package com.hyf.iot.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.Constant
import com.hyf.iot.common.Constant.KEY_PARAM_ID
import com.hyf.iot.common.Constant.KEY_PARAM_LAT
import com.hyf.iot.common.Constant.KEY_PARAM_LONG
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.FarmDetailContract
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.presenter.FarmDetailPresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_farm_detail.*
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * 农场详情
 *
 * */
class FarmDetailActivity : BaseMvpActivity<FarmDetailContract.IPresenter>(),FarmDetailContract.IView {

    private var id: String = ""
    private var data: Farm? = null
    private val mLoadingDialog by lazy { LoadingDialog(this) }

    override fun registerPresenter() = FarmDetailPresenter::class.java

    override fun showDetail(data: Farm) {
        this.data = data
        tv_farm_name.text = data.name
        tv_farm_address.text = data.address
    }

    override fun getLayoutId() = R.layout.activity_farm_detail

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.farm_detail)
        tv_operate.visibility = View.VISIBLE
        tv_operate.text = "编辑"
        tv_operate.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_PARAM_ID,data!!.id)
            bundle.putDouble(KEY_PARAM_LAT,data!!.latitude)
            bundle.putDouble(KEY_PARAM_LONG,data!!.longitude)
            bundle.putString(Constant.KEY_PARAM_ADDRESS,data!!.address)
            bundle.putString(Constant.KEY_PARAM_NAME,data!!.name)
            newIntent<FarmAddOrEditActivity>(ON_SUCCESS,bundle)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val bundle = intent.getBundleExtra("bundle")
        if(bundle != null) {
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
        if(resultCode == FragmentActivity.RESULT_OK){
            if(requestCode == ON_SUCCESS){
                initData()
            }
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

}