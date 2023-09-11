package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.flow.flow

class BaseConfigurationRepository(application: Application) {

    private val databaseRepository by lazy {
        DatabaseRepsoitory(application)
    }


    fun setUpBaseConfig() = flow {
        emit(ApiResponse.Loading("Loading From DataBase"))
        val data = try {
            val result = databaseRepository.getDeviceId("62")
                //databaseRepository.getUserRegNo("NAVIK200-1.0")
            if (result.isNotEmpty()){
                ApiResponse.Success(result)
            }else {
                ApiResponse.Error("Connect Fetch Response",null)
            } ?: ApiResponse.Error("Connect Fetch Response", null)
            //ApiResponse.Success()
        } catch (e: Exception) {
            ApiResponse.Error(e, null)
        }
        emit(data)
    }


}