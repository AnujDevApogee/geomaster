package com.apogee.geomaster.model

sealed class DynamicViewType {
    data class SpinnerData(
        val hint: String
    ) : DynamicViewType()

    data class EditText(
        val hint: String
    ) : DynamicViewType()

    companion object {
        val list = listOf(
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            SpinnerData(
                "Toogle Controller"
            ),
            EditText(
                "My Edit Text"
            ),
            EditText(
                "My Edit Text"
            ),
            EditText(
                "My Edit Text1"
            ),
            EditText(
                "My Edit Text2"
            )
        )
    }
}
