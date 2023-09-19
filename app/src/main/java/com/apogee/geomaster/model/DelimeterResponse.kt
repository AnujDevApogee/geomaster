package com.apogee.geomaster.model

import com.apogee.geomaster.response_handling.model.SateliteTypeModel


class DelimeterResponse(
    var validation_value: String,
    var validation_index: String,
    var remark: String,
    var type: String,
    var satelliteTypeModelArrayList: ArrayList<SateliteTypeModel>
) {
    override fun toString(): String {
        return "DelimeterResponse{" +
                "validation_value='$validation_value'" +
                ", validation_index='$validation_index'" +
                ", remark='$remark'" +
                ", type='$type'" +
                ", satelliteTypeModelArrayList=$satelliteTypeModelArrayList" +
                "}"
    }
}

