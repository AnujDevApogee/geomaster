package com.apogee.geomaster.model.MultiView

import android.view.View

interface OnItemValueListener {

    fun returnValue(title: String?, finalvalue: String?)
    fun returnValue(
        title: String?,
        finalvalue: String?,
        position: Int,
        operation: String?,
        elevation: String?
    )

    interface OnClickRecyclerListner {
        fun onClick(view: View?)
    }
}