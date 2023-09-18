package com.apogee.geomaster.response_handling.model

class SateliteTypeModel(
    val satelite_type: String,
    val start_prn: String,
    val end_prn: String
) {
    override fun toString(): String {
        return "SateliteTypeModel{" +
                "satelite_type='$satelite_type', " +
                "start_prn='$start_prn', " +
                "end_prn='$end_prn'" +
                "}"
    }
}
