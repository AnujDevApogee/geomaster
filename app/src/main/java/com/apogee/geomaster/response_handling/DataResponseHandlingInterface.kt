package com.apogee.geomaster.bluetooth

import com.apogee.basicble.Utils.MultiMap
import com.apogee.geomaster.response_handling.model.ResponseHandlingModel


interface DataResponseHandlingInterface {
    fun gsaRecieveData(data : ArrayList<ResponseHandlingModel>)
    fun gsvRecieveData(data : ArrayList<ResponseHandlingModel>)
    fun fixResponseData(validate_res_map: MultiMap<String, String>)
    fun ackRecieveData(status: Int)
}