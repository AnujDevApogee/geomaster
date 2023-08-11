package com.apogee.geomaster.model

sealed class DynamicViewType {
    data class SpinnerData(
        val id: Int,
        val hint: String
    ) : DynamicViewType()

    data class EditText(
        val id: Int,
        val hint: String
    ) : DynamicViewType()

    companion object {
        val list = listOf(
            SpinnerData(
                0,
                "Toogle Controller"
            ),
            SpinnerData(
                1,
                "Toogle Controller"
            ),
            SpinnerData(
                2,
                "Toogle Controller"
            ),
            SpinnerData(
                3,
                "Toogle Controller"
            ),
            SpinnerData(
                4,
                "Toogle Controller"
            ),
            SpinnerData(
                5,
                "Toogle Controller"
            ),
            SpinnerData(
                6,
                "Toogle Controller"
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
