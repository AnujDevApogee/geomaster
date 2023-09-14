package com.apogee.geomaster.model

data class RadioConnection(
    val togglePreviousConfiguration: String,
    val dataRate: Pair<String, String>,
    val power: Pair<String, String>,
    val frequency: Pair<String, String>,
    val baudRate: Pair<String, String>
)