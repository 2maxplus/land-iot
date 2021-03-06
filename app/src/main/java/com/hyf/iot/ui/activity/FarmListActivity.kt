package com.hyf.iot.ui.activity

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.adapter.farm.FarmAdapter
import com.hyf.iot.common.Constant.KEY_PARAM_1
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.FarmListContract
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.presenter.FarmListPresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.ItemDecoration
import com.hyf.iot.widget.PageStateLayout
import com.hyf.iot.widget.dialog.LoadingDialog
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*

/**
 *  农场list
 **/

class FarmListActivity : BaseMvpActivity<FarmListContract.IPresenter>(), FarmListContract.IView {


    private val mAdapter by lazy { FarmAdapter(this, arrayListOf()) }

    private val mLoadingDialog by lazy { LoadingDialog(this) }

    override fun registerPresenter() = FarmListPresenter::class.java

    private var mTitle: String? = "我的农场"

    override fun getLayoutId() = R.layout.activity_farm_list

    override fun initView() {
        tv_title.text = mTitle
        iv_back.setOnClickListener { onBack() }
        page_layout.apply {
            setContentView(View.inflate(context, R.layout.layout_recycler_view, null))
            setOnPageErrorClickListener { onReload() }
        }

        recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
            addItemDecoration(ItemDecoration(2))
        }

        mAdapter.setCallback(object : FarmAdapter.Callback {
            override fun click(v: View, item: Farm) {
                LoginUser.farmId = mAdapter.getCheckedId()
                LoginUser.farmName = mAdapter.getCheckedName()
                intent.putExtra(KEY_PARAM_1,item)
                setResult(FragmentActivity.RESULT_OK,intent)
                finish()
//                val dialog = NormalMsgDialog(this@BindingListActivity).setMessage("确认切换当前农场？")
//                        .setRightButtonInfo("", DialogInterface.OnClickListener{ _, _ ->
//                            mLoadingDialog.show()
//                            getPresenter().unbindUserById(id)
//                        })
//                dialog.show()
            }
        })

        tv_operate.visibility = View.VISIBLE
        tv_operate.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.fab_add,0,0)
        tv_operate.setOnClickListener {
           newIntent<FarmAddOrEditActivity>()
        }
    }

    override fun initData() {
        getPresenter().getFarmList()
    }


    private fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        initData()
    }

    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun onError(errorMsg: String?) {
        mLoadingDialog.dismiss()
        page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }

    override fun showPageList(data: MutableList<Farm>) {
        if (data.isEmpty()) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            mAdapter.setData(data)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == ON_SUCCESS){
                initData()
            }
        }
    }

}