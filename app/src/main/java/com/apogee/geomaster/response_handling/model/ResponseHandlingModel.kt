package com.apogee.basicble.Utils

class ResponseHandlingModel(
    val key: String,
    val multiMap: MultiMap<String,String>
) {
    override fun toString(): String {
        return "ResponseHandlingModel{" +
                "key='$key', " +
                "value=$multiMap" +
                "}"
    }
}
