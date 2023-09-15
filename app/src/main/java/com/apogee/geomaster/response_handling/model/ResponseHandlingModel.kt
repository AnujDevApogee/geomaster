package com.apogee.geomaster.response_handling.model

import com.apogee.basicble.Utils.MultiMap

class ResponseHandlingModel(
    val key: String,
    val multiMap: MultiMap<String, String>
) {
    override fun toString(): String {
        return "ResponseHandlingModel{" +
                "key='$key', " +
                "value=$multiMap" +
                "}"
    }
}
