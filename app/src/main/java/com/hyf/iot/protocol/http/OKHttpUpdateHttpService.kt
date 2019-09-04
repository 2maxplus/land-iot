package com.hyf.iot.protocol.http

import com.hyf.iot.common.LoginUser
import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import com.zhy.http.okhttp.callback.StringCallback

import java.io.File
import java.util.TreeMap

import okhttp3.Call
import okhttp3.Request
import okhttp3.Response

/**
 * 使用okhttp
 *
 */
class OKHttpUpdateHttpService : IUpdateHttpService {
    init {
        OkHttpUtils.getInstance().timeout(20000)
    }


    override fun asyncGet(url: String, params: Map<String, Any>, callBack: IUpdateHttpService.Callback) {
//        enqueue(getRequest(url, mapOf("method" to HttpMethod.GET),transform(params)), object: Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                callBack.onError(e)
//            }
//            override fun onResponse(call: Call, response: Response) {
//               callBack.onApplySuccess(response.body().toString())
//            }
//        })

        OkHttpUtils.get()
                .url(url)
                .params(transform(params))
                .addHeader("token",LoginUser.token)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, response: Response, e: Exception, id: Int) {
                        callBack.onError(e)
                    }

                    override fun onResponse(response: String, id: Int) {
                        callBack.onSuccess(response)
                    }
                })
    }

    override fun asyncPost(url: String, params: Map<String, Any>, callBack: IUpdateHttpService.Callback) {
        OkHttpUtils.post()
                .url(url)
                .params(transform(params))
                .addHeader("token",LoginUser.token)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, response: Response, e: Exception, id: Int) {
                        callBack.onError(e)
                    }

                    override fun onResponse(response: String, id: Int) {
                        callBack.onSuccess(response)
                    }
                })
    }

    override fun download(url: String, path: String, fileName: String, callback: IUpdateHttpService.DownloadCallback) {
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(object : FileCallBack(path, fileName) {
                    override fun inProgress(progress: Float, total: Long, id: Int) {
                        callback.onProgress(progress, total)
                    }

                    override fun onError(call: Call, response: Response, e: Exception, id: Int) {
                        callback.onError(e)
                    }

                    override fun onResponse(response: File, id: Int) {
                        callback.onSuccess(response)
                    }

                    override fun onBefore(request: Request?, id: Int) {
                        super.onBefore(request, id)
                        callback.onStart()
                    }
                })
    }

    override fun cancelDownload(url: String) {

    }

    private fun transform(params: Map<String, Any>): Map<String, String> {
        val map = TreeMap<String, String>()
        for ((key, value) in params) {
            map[key] = value.toString()
        }
        return map
    }


}
