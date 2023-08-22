package com.apogee.geomaster.model

data class SatelliteModel(
    val satelliteName: String,
    var satelliteStatus: String
)

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
