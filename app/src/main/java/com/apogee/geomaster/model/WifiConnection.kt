package com.apogee.geomaster.model

data class WifiConnection(
    val airDataRate: String,
    val frequency: String,
    val protocol: String,
    val power: String,
    val togglePreviousButton: String
) {
    companion object {
        val list = listOf(
            WifiConnection(
                "9600",
                "123.342",
                "TRIM_THINK_13",
                "0",
                "1"
            ), WifiConnection(
                "9600",
                "123.342",
                "TRIM_THINK_13",
                "0",
                "1"
            )
        )
    }
}