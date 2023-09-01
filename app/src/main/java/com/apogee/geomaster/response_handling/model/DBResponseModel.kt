package com.apogee.basicble.Utils

class DBResponseModel(
    val response: String,
    val response_id: String,
    val response_type_id: String,
    val data_extract_type: String,
    val command_accepted: String,
    var flag: Int = 0,
    val parameterList: ArrayList<String>,
    val delimeterList: ArrayList<DelimeterResponse>
) {


    override fun toString(): String {
        return "ResponseModel{" +
                "response='$response', " +
                "response_id='$response_id', " +
                "response_type_id='$response_type_id', " +
                "data_extract_type='$data_extract_type', " +
                "command_accepted='$command_accepted', " +
                "flag=$flag, " +
                "parameterList=$parameterList, " +
                "delimeterList=$delimeterList" +
                "}"
    }
}
