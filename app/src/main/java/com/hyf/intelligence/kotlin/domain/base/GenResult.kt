package com.hyf.intelligence.kotlin.domain.base



data class GenResult<T>(
    val code: Int,
    val data: T,
    val msg: String)

