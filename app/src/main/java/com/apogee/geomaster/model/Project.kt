package com.apogee.geomaster.model

data class Project(
    var title: String,
    val dataumName: String,
    val projectionType: String,
    val zone: String
)
