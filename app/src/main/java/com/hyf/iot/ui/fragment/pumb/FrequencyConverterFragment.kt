package com.hyf.iot.ui.fragment.pumb

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PumpItemFragmentAdapter
import com.hyf.iot.common.CP
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpItemContract
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.domain.pumb.FrequencyConverterCabinetInfo
import com.hyf.iot.presenter.PumpItemPresenter
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.frequency_converter_layout.*

class FrequencyConverterFragment : BaseMvpFragment<PumpItemContract.IPresenter>(), PumpItemContract.IView {

    private var isRefresh = false
    companion object {
        const val INTENT_ACTION_REFRESH = "com.action.refresh"
    }

    override fun onTokenExpired(msg: String) {
    }

    private val mAdapter by lazy { PumpItemFragmentAdapter(childFragmentManager, mutableListOf()) }

    override fun getLayoutId(): Int = R.layout.frequency_converter_layout

    override fun initView() {
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p: Int) {
                    currentItem = p
                    if(!isRefresh) {
                        CP.currentItem = p
                    }
                    isRefresh = false
                }
            })
        }
        tabLayout.apply {
            setupWithViewPager(viewPager)
        }

        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(context!!,
                R.drawable.layout_divider_vertical)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        isRefresh = arguments!!.getBoolean("isRefresh")
        val frequencyConverterCabinet = arguments!!.getParcelable<FrequencyConverterCabinetInfo>("data")
                ?: return
        tv_frequency_name.text = frequencyConverterCabinet.name
        tv_current1.text = "电流A: ${frequencyConverterCabinet.currentA} A"
        tv_current2.text = "电流B: ${frequencyConverterCabinet.currentB} A"
        tv_current3.text = "电流C: ${frequencyConverterCabinet.currentC} A"
        tv_voltage1.text = "电压A: ${frequencyConverterCabinet.voltageA} V"
        tv_voltage2.text = "电压B: ${frequencyConverterCabinet.voltageB} V"
        tv_voltage3.text = "电压C: ${frequencyConverterCabinet.voltageC} V"
        tv_total_power.text = "总输出功率: ${frequencyConverterCabinet.totalOutputPower} W"
        getPresenter().getPumpItemInfo(frequencyConverterCabinet.id)
    }

    override fun registerPresenter() = PumpItemPresenter::class.java

    override fun showPage(data: MutableList<WaterPump>) {
        mAdapter.fragmentList.clear()
        if (tabLayout != null) {
            if (data.size <= 1)
                tabLayout.visibility = View.GONE
        }
        viewPager.offscreenPageLimit = data.size
        mAdapter.fragmentList.addAll(data)
        mAdapter.notifyDataSetChanged()
        if (childFragmentManager.fragments.size > 0) {
            viewPager.currentItem = CP.currentItem
            ((childFragmentManager.fragments[viewPager.currentItem]) as PumpItemFragment).initData()
        }
//        activity?.sendBroadcast(Intent(PumpItemFragment.INTENT_ITEM_ACTION_REFRESH))
    }

    override fun errorPage(msg: String?) {
        activity?.showToast(msg!!)
    }

    lateinit var receiveBroadCast: ReceiveBroadCast
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        receiveBroadCast = ReceiveBroadCast()
        val intentFilter = IntentFilter(INTENT_ACTION_REFRESH)
        activity?.registerReceiver(receiveBroadCast, intentFilter)
    }

    override fun onDetach() {
        super.onDetach()
        if (receiveBroadCast != null) {
            activity?.unregisterReceiver(receiveBroadCast)
        }
    }

    inner class ReceiveBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            this@FrequencyConverterFragment.initData()
        }
    }

}