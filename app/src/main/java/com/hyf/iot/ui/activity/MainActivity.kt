package com.hyf.iot.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.widget.Toast
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.adapter.MainTabAdapter
import com.hyf.iot.common.Constant
import com.hyf.iot.common.HTTP_API_DOWNLOAD_RELEASE
import com.hyf.iot.domain.TabBean
import com.hyf.iot.protocol.http.CustomUpdateParser
import com.hyf.iot.ui.fragment.main.HomeFragment
import com.hyf.iot.ui.fragment.main.MyFragment
import com.hyf.iot.ui.fragment.main.ValveControlFragment
import com.hyf.iot.ui.fragment.plan.PlanParentsFragment
import com.hyf.iot.ui.fragment.plans.PlanFragment
import com.hyf.iot.ui.fragment.pumb.PumpRoomFragment
import com.xuexiang.xupdate.XUpdate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    private var mFirstDownBack: Long = 0L
    private var mCurIndex: Int = 0

    private val mFragments = listOf(
            HomeFragment(),
            PumpRoomFragment(),
            ValveControlFragment(),
//            PlanFragment(),
            PlanParentsFragment(),
            MyFragment())

    private val mTabList = listOf(
            TabBean(R.drawable.home_selector, R.string.tab1),
            TabBean(R.drawable.pumb_selector, R.string.tab2),
            TabBean(R.drawable.fakong_selector, R.string.tab3),
            TabBean(R.drawable.plan_selector, R.string.tab4),
            TabBean(R.drawable.mine_selector, R.string.tab5))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { supportFragmentManager.popBackStackImmediate(null, 1) }
        setContentView(R.layout.activity_main)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        tgv_group.setOnItemClickListener { openTabFragment(it) }
        tgv_group.setAdapter(MainTabAdapter(this, mTabList))
        openTabFragment(savedInstanceState?.getInt("index") ?: 0)
        XUpdate.newBuild(this)
                .topResId(R.drawable.xupdate_bg_app_top)
                .themeColor(resources.getColor(R.color.colorPrimary))
                .updateUrl(HTTP_API_DOWNLOAD_RELEASE)
                .updateParser(CustomUpdateParser(false)) //设置自定义的版本更新解析器
                .update()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", mCurIndex)
    }

    private fun openTabFragment(position: Int) {
        tgv_group.setSelectedPosition(position)
        val ft = supportFragmentManager.beginTransaction()
        ft.hide(mFragments[mCurIndex])
        var f = supportFragmentManager.findFragmentByTag(mFragments[position].javaClass.simpleName)
        if (f == null) {
            f = mFragments[position]
            ft.add(R.id.fl_content, f, f.javaClass.simpleName)
        }
        ft.show(f).commit()
        mCurIndex = position
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mFirstDownBack > 2000) {
            Toast.makeText(this, R.string.exit_go_out, Toast.LENGTH_SHORT).show()
            mFirstDownBack = System.currentTimeMillis()
            return
        } else {
            App.instance.exitApp()
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.RequestKey.REQUEST_GPS_CODE -> {
                (mFragments[0] as HomeFragment).requestLocation()
            }
        }

    }

}