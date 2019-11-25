package com.hyf.iot.widget.spinner

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.widget.RecycleViewDivider
import java.util.ArrayList


class SpinnerUtils(private val mContext: Context, private val mTextView: TextView, private val mData: ArrayList<String>, private val itemClickListener: SpinnerChooseAdapter.MyItemClickListener, private val selectPosition: Int) {

    fun showPopupWindow() {
        //        tvSetImg(mTextView, R.mipmap.arrow_top);
        val view = LayoutInflater.from(mContext).inflate(R.layout.choose_pop, null)
        val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.isTouchable = true
        popupWindow.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.shape_round_white))
        popupWindow.showAsDropDown(mTextView, 0, 15)
        popupWindow.setOnDismissListener(PopupDismissListener())
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_choose_pop)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(mContext)
        val adapter = SpinnerChooseAdapter(mContext, object : SpinnerChooseAdapter.MyItemClickListener {
            override fun onClick(view: View) {
                popupWindow.dismiss()
                itemClickListener.onClick(view)

            }
        }, mData, selectPosition)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(RecycleViewDivider(mContext, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL))
        popupWindow.update()
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.animationStyle = R.style.popmenu_animation
    }

    /**
     * 弹窗消失的时候让箭头换回来
     */
    internal inner class PopupDismissListener : PopupWindow.OnDismissListener {
        override fun onDismiss() {
            //            tvSetImg(mTextView, R.mipmap.arrow_down);
        }

    }

    /**
     * 设置textView右侧的图像
     *
     * @param textView
     * @param img
     */
    private fun tvSetImg(textView: TextView, img: Int) {
        val nav_up = mContext.resources.getDrawable(img)
        nav_up.setBounds(0, 0, nav_up.minimumWidth, nav_up.minimumHeight)
        textView.setCompoundDrawables(null, null, nav_up, null)
    }
}