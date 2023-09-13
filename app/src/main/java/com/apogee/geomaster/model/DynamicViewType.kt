package com.apogee.geomaster.model

sealed class DynamicViewType {
    data class SpinnerData(
        val id: Int,
        val hint: String,
        val dataList: List<String>,
        val valueList: List<String>,
        var selectedPair: Pair<String, String>? = null
    ) : DynamicViewType()

    data class EditText(
        val id: Int,
        val hint: String
    ) : DynamicViewType()

    companion object {
        val list = listOf(
            SpinnerData(
                0,
                "Toogle Controller",
                listOf(),
                listOf()
            ),
            EditText(
                7,
                "My Edit Text"
            ),
            EditText(
                8,
                "My Edit Text"
            ),
            EditText(
                9,
                "My Edit Text1"
            ),
            EditText(
                10,
                "My Edit Text2"
            )
        )
    }
}
