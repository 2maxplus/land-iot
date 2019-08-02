package com.hyf.iot.domain.user

data class VerifyBean(
        val created: String,
        val id: String,
        val number: String,
        val phone: String,
        val state: Int,
        val stateString: String,
        val updated: String,
        val accountId: String
)