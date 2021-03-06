package com.hyf.iot.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import com.hyf.iot.R
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_account_info.*
import kotlinx.android.synthetic.main.layout_common_title.*
import android.app.Activity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hyf.iot.App
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.UserContract
import com.hyf.iot.domain.user.UserInfo
import com.hyf.iot.presenter.UserPresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.dialog.MyDialog
import com.ljb.kt.HttpConfig
import com.luck.picture.lib.tools.PictureFileUtils

//
class AccountInfoActivity : BaseMvpActivity<UserContract.IPresenter>(), UserContract.IView {

    private val MODIFY_INFO_REQUEST = 101

    override fun registerPresenter() = UserPresenter::class.java

    private lateinit var dialogs: MyDialog

    override fun getLayoutId() = R.layout.activity_account_info

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.my_account)
        layout_nickname.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", nickname.text.toString())
            newIntent<ModifyNickNameActivity>(MODIFY_INFO_REQUEST, bundle)
        }
        rl_portrait.setOnClickListener {
            // 进入相册 以下是例子：用不到的 api 可以不写
            PictureSelector.create(this@AccountInfoActivity)
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认 jpeg
                    .enableCrop(true)// 是否裁剪 true or false
                    .glideOverride(160, 160)
                    .compress(true)
                    .freeStyleCropEnabled(true)
                    .circleDimmedLayer(true) // 是否圆形裁剪 true or false
                    .showCropFrame(false) // 是否显示裁剪矩形边框 圆形裁剪时建议设为 false   true or false
                    .showCropGrid(false) // 是否显示裁剪矩形网格 圆形裁剪时建议设为 false    true or false
                    .synOrAsy(false)//同步 true 或异步 false 压缩 默认同步
                    .scaleEnabled(true) // 裁剪是否可放大缩小图片 true or false
                    .isDragFrame(true) // 是否可拖动裁剪框(固定)
                    .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调0
        }

        btn_logout.setOnClickListener {
            dialogs = MyDialog(this, "确定退出当前账号吗？", View.OnClickListener {
                when (it.id) {
                    R.id.left_text -> {  //
                        dialogs.dismiss()
                    }
                    R.id.right_text -> {
                        //退出登录
                        getPresenter().logout()
                        dialogs.dismiss()
                    }
                }
            })
            dialogs.show()
        }
    }


    override fun initData() {
        getPresenter().getUserInfo()
        getPresenter().getPortrait()
    }

    override fun showUserInfo(user: UserInfo) {
        nickname.text = user.nickName
        telephone.text = user.userName
    }

    override fun logoutSuccess() {
        LoginUser.token = ""
        val headerMap = HashMap<String, String>()
        HttpConfig.setHeader(headerMap)
        goLogin()
    }

    private fun goLogin() {
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

    override fun portraitModifySuccess(headData: String) {
        PictureFileUtils.deleteCacheDirFile(this)
        Glide
                .with(this)
                .load(headData)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(iv_head)
    }

    override fun showPortrait(url: String) {
        Glide
                .with(this)
                .load(url)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(iv_head)

    }

    override fun onTokenExpired(msg: String) {
        showToast(msg)
        goLogin()
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            PictureConfig.CHOOSE_REQUEST -> {
                val selectList = PictureSelector.obtainMultipleResult(data)
                val media = selectList[0]
                val path = media.compressPath
                Glide
                        .with(this)
                        .load(path)
                        .apply(RequestOptions().transform(CircleCrop()))
                        .into(iv_head)
                getPresenter().modifyPortrait(path)
            }
            MODIFY_INFO_REQUEST -> { //昵称修改
                if (data != null) {
                    nickname.text = data.getStringExtra("nickname")
                }
            }
        }

    }

}