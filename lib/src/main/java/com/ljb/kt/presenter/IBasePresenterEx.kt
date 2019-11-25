package com.ljb.kt.presenter

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

fun IBasePresenter<*>.getContextEx(): Context = when {
    getMvpView() is Activity -> getMvpView() as Activity
    getMvpView() is androidx.fragment.app.Fragment -> (getMvpView() as androidx.fragment.app.Fragment).activity!!
    else -> throw IllegalStateException("the presenter not found context")
}
