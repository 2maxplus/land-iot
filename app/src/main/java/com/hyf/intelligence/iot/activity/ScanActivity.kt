package com.hyf.intelligence.iot.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.bingoogolapple.qrcode.core.QRCodeView
import kotlinx.android.synthetic.main.layout_scan.*
import android.os.Vibrator
import android.content.Context
import cn.bingoogolapple.qrcode.core.BarcodeType
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.common.activity.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*


/***
 * scan qr-code
 *
 * */
class ScanActivity: BaseActivity() , QRCodeView.Delegate {

    private val REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666


    override fun getLayoutId() = R.layout.layout_scan

    override fun initSaveInstanceState(saveInstanceState: Bundle?) {

    }

    override fun initView() {
        iv_back.setOnClickListener { finish() }
        tv_title.text = "扫描"
        mZXingView.setDelegate(this)

    }

    override fun initData() {

    }

    override fun onStart() {
        super.onStart()
        mZXingView.startCamera()
        mZXingView.changeToScanQRCodeStyle()
        mZXingView.setType(BarcodeType.ALL,null)
        mZXingView.startSpotAndShowRect()
    }

    override fun onStop() {
        super.onStop()
        mZXingView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        mZXingView.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        vibrate()
        mZXingView.startSpot() // 开始识别
        intent.putExtra("result",result)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = mZXingView.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                mZXingView.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mZXingView.startSpotAndShowRect()// 显示扫描框，并开始识别

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0)
//            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//            mZXingView.decodeQRCode(picturePath)

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
        }

    }
}