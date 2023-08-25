package com.apogee.geomaster.model

data class Project(
    var title: String,
    val configurationName: String,
    /*    val projectionType: String,
        val zone: String*/
)

data class ConfigSetup(
    var configurationName: String,
    val datumName: String,
    val workMode: String
)
