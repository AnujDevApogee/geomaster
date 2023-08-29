package com.apogee.geomaster.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SatelliteModel(
    val satelliteIndex: String,
    val satelliteName: String,
    var satelliteStatus: String
) : Parcelable

/*{
    companion object {
        val list = listOf(
            SatelliteModel(
                "Galileo Satellite"
            ), SatelliteModel(
                "Beidou Satellite"
            ), SatelliteModel(
                "Navik Satellite"
            ), SatelliteModel(
                "GPS"
            )
        )
    }
}*/
