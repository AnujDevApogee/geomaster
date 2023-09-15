package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.createLog
import kotlinx.coroutines.flow.flow

class BaseCommandRepository(application: Application) {

    private val dbDataRepository by lazy {
        DatabaseRepsoitory(application)
    }

    fun getCommandBle(operationName: String, rtk: String, dgps: Int) = flow {
        emit(ApiResponse.Loading("Please Wait Loading Operation.."))
        val data = try {
            getData(operationName, rtk, dgps)
        } catch (e: Exception) {
            ApiResponse.Error(null, e)
        }
        emit(data)
    }

    private fun getData(operationName: String, rtk: String, dgps: Int): ApiResponse<out Any> {
        val oPLs = dbDataRepository.getOperationIDBLE(operationName, rtk)
        createLog("TAG_BLE_CMD","Operation Name $oPLs")
        if (oPLs.isEmpty()) {
            return ApiResponse.Error("Empty Operation List Found", null)
        }
        val opLsString =
            BaseConfigurationRepository.getSubString(oPLs.toString(), 1, oPLs.toString().length-1)
        val cmdIdList = dbDataRepository.getCommandListBLE(opLsString, dgps)
        createLog("TAG_BLE_CMD","Operation CMD_ID_LIST -> $cmdIdList")
        if (cmdIdList.isEmpty()) {
            return ApiResponse.Error("Empty Command List Found", null)
        }
        val cmdIdLsString = BaseConfigurationRepository.getSubString(
            cmdIdList.toString(),
            1,
            cmdIdList.toString().length-1
        )
        createLog("TAG_BLE_CMD","Operation CMD_LIST_STRING -> $cmdIdLsString")
        val cmdListLs = dbDataRepository.getCommandList(cmdIdLsString)
        createLog("TAG_BLE_CMD","Operation CMD_NAME_LIST -> $cmdListLs")
        if (cmdListLs.isEmpty()) {
            return ApiResponse.Error("Empty Command-ID List Found", null)
        }
        return ApiResponse.Success(cmdListLs.toList())
    }


}