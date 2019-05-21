package com.hyf.iot.domain.user

data class LoginInfo(
        val created: String,
        val expire: String,
        val id: String,
        val isDeleted: Boolean,
        val token: String,
        val accountType: String,  //操作员 = 1,农场管理员 = 10,系统管理员 = 100,
        val updated: String,
        val userId: String
)