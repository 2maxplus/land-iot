package com.hyf.iot.domain.user

data class LoginInfo(
        val tokenInfo: AccountToken,
        val accountType: String   //操作员 = 1,农场管理员 = 10,系统管理员 = 100,
)

data class AccountToken(
        val name:String?,
        val accountId: String,
        val expire: String,
        val id: String,
        val token: String
)