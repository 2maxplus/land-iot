package com.hyf.intelligence.kotlin.domain.user

data class VerifyBean(
        val created: String,
        val id: String,
        val number: String,
        val phone: String,
        val state: Int,
        val stateString: String,
        val updated: String,
        val userId: String
)