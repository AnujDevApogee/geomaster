package com.apogee.geomaster.utils

import android.content.Context
import android.widget.Toast

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun isInvalidString(txt: String?) = txt.isNullOrEmpty() || txt.isBlank() || txt == "null"