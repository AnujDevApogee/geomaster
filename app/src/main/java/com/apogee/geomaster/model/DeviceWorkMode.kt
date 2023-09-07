package com.apogee.geomaster.model

data class DeviceMode(
    val type: String,
    val position: Int,
) {
    companion object {
        val list = listOf(
            DeviceMode(
                "BASE", 1
            ), DeviceMode(
                "ROVER", 2
            ), DeviceMode(
                "STATIC", 3
            ), DeviceMode(
                "PPK", 4
            )
        )
    }

}