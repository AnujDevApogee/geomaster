package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.createLog
import kotlinx.coroutines.flow.flow

class BaseConfigurationRepository(application: Application) {

    private val databaseRepository by lazy {
        DatabaseRepsoitory(application)
    }


    fun setUpBaseConfig(deviceName: String) = flow {
        emit(ApiResponse.Loading("Loading From DataBase"))
        val data = try {
            val resultUserRegNo = databaseRepository.getUserRegNo("NAVIK200-1.0")
            resultUserRegNo?.let { id ->
                val resultDeviceId = databaseRepository.getDeviceId(id)
                createLog("DB_INFO", "UserRegNo $id")
                createLog("DB_INFO", "DeviceId  $resultDeviceId")
                if (resultDeviceId.isNotEmpty()) {
                    val makeName =
                        databaseRepository.getMakeName(resultDeviceId["ManufactureID"]!!.toInt())
                    createLog("DB_INFO", "MakeName $makeName")
                    val makeResponse =
                        databaseRepository.getModuleFinishedId(resultDeviceId["DeviceID"].toString())
                    createLog(
                        "DB_INFO",
                        "ModuleFinished ${
                            getSubString(
                                makeResponse.toString(),
                                1,
                                makeResponse.toString().length - 1
                            )
                        }"
                    )
                    if (makeResponse.isNotEmpty()) {
                        val deviceModule = databaseRepository.getDeviceModule(
                            getSubString(
                                makeResponse.toString(),
                                1,
                                makeResponse.toString().length - 1
                            )
                        )
                        createLog("DB_INFO","get DeviceModule $deviceModule")
                        ApiResponse.Success(Pair(makeName, deviceModule))
                    } else {
                        ApiResponse.Error("Connect Fetch Response Connection Set up type", null)
                    }
                } else {
                    ApiResponse.Error("Connect Fetch Response DeviceId", null)
                }
            } ?: ApiResponse.Error("Connect Fetch Response $deviceName info", null)
        } catch (e: Exception) {
            ApiResponse.Error(e, null)
        }
        emit(data)
    }

    private fun getSubString(str: String, from: Int, to: Int): String {
        return str.substring(from, to).trim()
    }

}