package com.hyf.iot.domain.user

data class UserInfo(
        val created: String,
        val headPortrait: String,
        val nickName: String,
        val type: Int,
        val typeString: String,
        val userName: String
)