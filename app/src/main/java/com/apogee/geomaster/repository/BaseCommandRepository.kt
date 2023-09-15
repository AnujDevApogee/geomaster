package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.flow.flow

class BaseCommandRepository(application: Application) {

    private val dbDataRepository by lazy {
        DatabaseRepsoitory(application)
    }

    fun getCommandBle(operationIds: String, rtk: String, dgps: Int) = flow {
        emit(ApiResponse.Loading("Please Wait Loading Operation.."))
        val data = try {
            getData(operationIds, rtk, dgps)
        } catch (e: Exception) {
            ApiResponse.Error(null, e)
        }
        emit(data)
    }

    private fun getData(operationIds: String, rtk: String, dgps: Int): ApiResponse<out Any> {
        val oPLs = dbDataRepository.getOperationIDBLE(operationIds, rtk)
        if (oPLs.isEmpty()) {
            return ApiResponse.Error("Empty Operation List Found", null)
        }
        val opLsString =
            BaseConfigurationRepository.getSubString(oPLs.toString(), 1, oPLs.toString().length)

        val cmdIdList = dbDataRepository.getCommandListBLE(opLsString, dgps)
        if (cmdIdList.isEmpty()) {
            return ApiResponse.Error("Empty Command List Found", null)
        }
        val cmdIdLsString = BaseConfigurationRepository.getSubString(
            cmdIdList.toString(),
            1,
            cmdIdList.toString().length
        )

        val cmdListLs = dbDataRepository.getCommandList(cmdIdLsString)

        if (cmdListLs.isEmpty()) {
            return ApiResponse.Error("Empty Command-ID List Found", null)
        }
        return ApiResponse.Success(cmdListLs.toList())
    }


}