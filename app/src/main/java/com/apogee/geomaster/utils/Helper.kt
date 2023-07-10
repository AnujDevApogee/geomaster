package com.apogee.geomaster.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.apogee.geomaster.R
import com.google.gson.Gson

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

object ApiUtils {
    val POST_GET_TABLE_RECORDS =
        Pair("http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getAllTableRecords/", 102)
}

fun isInvalidString(txt: String?) = txt.isNullOrEmpty() || txt.isBlank() || txt == "null"


fun Activity.changeStatusBarColor(color: Int) {
    this.window?.statusBarColor = getColorInt(color)
}

fun Activity.getColorInt(color: Int): Int {
    return resources.getColor(color, null)
}

fun <T> toJson(t: T): String {
    return Gson().toJson(t)
}


inline fun <reified T> fromJson(str: String): T {
    return Gson().fromJson(str, T::class.java)
}