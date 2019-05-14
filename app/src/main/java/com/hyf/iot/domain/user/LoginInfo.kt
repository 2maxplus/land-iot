package com.hyf.iot.domain.user

data class LoginInfo(
        val created: String,
        val expire: String,
        val id: String,
        val isDeleted: Boolean,
        val token: String,
        val updated: String,
        val userId: String
)