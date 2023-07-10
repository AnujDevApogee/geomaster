package com.apogee.geomaster.utils

sealed class ApiResponse<T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Loading<T>(data: T?) : ApiResponse<T>(data)
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Error<T>(data: T? = null, exception: Exception?) : ApiResponse<T>(data, exception)
}