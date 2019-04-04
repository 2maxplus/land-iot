package com.hyf.intelligence.iot.domain.base



data class GenResult<T>(
    val code: Int,
    val data: T,
    val msg: String)

