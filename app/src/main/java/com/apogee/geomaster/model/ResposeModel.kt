package com.apogee.geomaster.model


class ResponseModel(
    var response: String,
    var response_id: String,
    var response_type_id: String,
    var data_extract_type: String,
    var command_accepted: String,
    var flag: Int = 0,
    var parameterList: ArrayList<String> = ArrayList(),
    var delimiterList: ArrayList<DelimeterResponse> = ArrayList()
) {
    override fun toString(): String {
        return "ResponseModel{" +
                "response='$response'" +
                ", response_id='$response_id'" +
                ", response_type_id='$response_type_id'" +
                ", data_extract_type='$data_extract_type'" +
                ", command_accepted='$command_accepted'" +
                ", flag=$flag" +
                ", parameterList=$parameterList" +
                ", delimiterList=$delimiterList" +
                "}"
    }
}
