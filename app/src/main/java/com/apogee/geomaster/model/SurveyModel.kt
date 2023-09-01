package com.apogee.geomaster.model


data class SurveyModel(
    val id: Int,
    val pointName: String,
    val codeName: String,
    val easting: Double,
    val northing: Double,
    val zone: Int,
    val elevation: Double,
    val prefix: String,
    val record_type: String,
    val survey_type: String
)