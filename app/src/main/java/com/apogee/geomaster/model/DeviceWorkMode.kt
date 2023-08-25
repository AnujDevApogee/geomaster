package com.apogee.geomaster.model

data class DeviceWorkMode(
    val type:String,
    val communicationType:String,
    val maskAngle:String
) {
    companion object{
        val list= listOf(
            DeviceWorkMode(
                "Rover Device",
                "Radio",
                "20 degree"
            )
        )
    }

}