package com.hyf.iot.protocol.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hyf.iot.BuildConfig
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.domain.AppVersion
import com.hyf.iot.domain.base.GenResult
import com.xuexiang.xupdate.entity.UpdateEntity
import com.xuexiang.xupdate.proxy.IUpdateParser

class CustomUpdateParser : IUpdateParser {
        override fun parseJson(json: String?): UpdateEntity? {
            val data = Gson().fromJson<GenResult<AppVersion>>(json,
                    object : TypeToken<GenResult<AppVersion>>() {}.type)
            if (data.code == RESULT_SUCCESS) {
                val result = data.data
                var hasUpdate = false
                if (result.versionCode > BuildConfig.VERSION_CODE)
                    hasUpdate = true
                return UpdateEntity()
                        .setHasUpdate(hasUpdate)  //是否有新版本
                        .setForce(result.isForcedUpdate)
                        .setVersionCode(result.versionCode)
                        .setVersionName(result.versionName)
                        .setUpdateContent(result.description)
                        .setDownloadUrl(result.downloadUrl)
            }
            return null
        }
    }