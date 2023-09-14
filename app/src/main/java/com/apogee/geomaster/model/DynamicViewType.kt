package com.apogee.geomaster.model

sealed class DynamicViewType {
    data class SpinnerData(
        val id: Int,
        val hint: String,
        val dataList: List<String>,
        val valueList: List<String>,
        var selectedPair: Pair<String, String>? = null //dataList and ValueList
    ) : DynamicViewType()

    data class EditText(
        val id: Int,
        val hint: String,
        var data: String?=null // EditText Value
    ) : DynamicViewType()
}
