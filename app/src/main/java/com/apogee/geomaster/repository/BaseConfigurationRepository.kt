package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.flow.flow
import java.io.Serializable

class BaseConfigurationRepository(application: Application) {

    private val databaseRepository by lazy {
        DatabaseRepsoitory(application)
    }


    fun setUpBaseConfig(deviceName: String) = flow {
        emit(ApiResponse.Loading("Loading From DataBase"))
        val data = try {
            getBaseResult(deviceName)
        } catch (e: Exception) {
            ApiResponse.Error(e, null)
        }
        emit(data)
    }

    private suspend fun getBaseResult(deviceName: String): ApiResponse<out Serializable> {
        val resultUserRegNo = databaseRepository.getUserRegNo(deviceName)
            ?: return ApiResponse.Error("Connect Fetch Response $deviceName info", null)

        val resultDeviceId = databaseRepository.getDeviceId(resultUserRegNo)

        if (resultDeviceId.isEmpty()) {
            return ApiResponse.Error("Connect Fetch Response DeviceId", null)
        }

        val makeName =
            databaseRepository.getMakeName(resultDeviceId["ManufactureID"]!!.toInt())
                ?: return ApiResponse.Error("Connect Fetch Manufacture Name", null)

        val makeResponse =
            databaseRepository.getModuleFinishedId(resultDeviceId["DeviceID"].toString())

        if (makeResponse.isEmpty()) {
            return ApiResponse.Error("Connect Fetch Response Connection Set up type", null)
        }

        val makeResLs = getSubString(
            makeResponse.toString(),
            1,
            makeResponse.toString().length - 1
        )
        val deviceModule = databaseRepository.getDeviceModule(makeResLs)

        return ApiResponse.Success(Pair(makeName, deviceModule))
    }

    private fun getSubString(str: String, from: Int, to: Int): String {
        return str.substring(from, to).trim()
    }

}