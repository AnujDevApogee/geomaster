package com.apogee.basicble.Utils

import com.apogee.geomaster.response_handling.model.SateliteTypeModel

class DelimeterResponse(
    val validation_value: String,
    val validation_index: String,
    val remark: String,
    val type: String,
    val sateliteTypeModelArrayList: ArrayList<SateliteTypeModel>
) {

    override fun toString(): String {
        return "DelimeterResponse{" +
                "validation_value='$validation_value', " +
                "validation_index='$validation_index', " +
                "remark='$remark', " +
                "type='$type', " +
                "sateliteTypeModelArrayList=$sateliteTypeModelArrayList" +
                "}"
    }
}
