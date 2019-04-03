package com.hyf.intelligence.kotlin.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

fun Context.showToast(message: String) : Toast {
    val toast : Toast = Toast.makeText(this,message,Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

fun Context.showToast(resId: Int) : Toast {
    val toast : Toast = Toast.makeText(this,getString(resId),Toast.LENGTH_SHORT)
    toast.show()
    return toast
}

inline fun <reified T: Activity> Activity.newIntent() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T: Activity> Activity.newIntent(bundle:Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("bundle",bundle)
    startActivity(intent)
}

inline fun <reified T: Activity> Activity.newIntentForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent,requestCode)
}

inline fun <reified T: Activity> Activity.newIntentForResult(requestCode: Int,bundle:Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("bundle",bundle)
    startActivityForResult(intent,requestCode)
}




