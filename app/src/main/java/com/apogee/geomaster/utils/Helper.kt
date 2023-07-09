package com.apogee.geomaster.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.apogee.geomaster.R

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun isInvalidString(txt: String?) = txt.isNullOrEmpty() || txt.isBlank() || txt == "null"


fun Activity.changeStatusBarColor(color: Int) {
    this.window?.statusBarColor = getColorInt(color)
}

fun Activity.getColorInt(color: Int): Int {
    return resources.getColor(color, null)
}