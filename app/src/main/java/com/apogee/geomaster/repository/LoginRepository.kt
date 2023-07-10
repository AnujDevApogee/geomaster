package com.apogee.geomaster.repository

import com.apogee.geomaster.utils.ApiResponse

import kotlinx.coroutines.flow.MutableSharedFlow

interface  LoginRepository {

    val data: MutableSharedFlow<ApiResponse<Any?>>

}