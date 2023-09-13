package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.createLog
import kotlinx.coroutines.flow.flow

class SetupConnectionRepository(application: Application) {


    private val databaseRepository by lazy {
        DatabaseRepsoitory(application)
    }


    fun getInputRequiredParma(operationName: String, dgps: Int) = flow {
        emit(ApiResponse.Loading("Please Setting Input"))
        val data = try {
            upInfoSetUp(operationName, dgps)
        } catch (e: Exception) {
            ApiResponse.Error(null, e)
        }
        emit(data)
    }

    private fun upInfoSetUp(operationName: String, dgps: Int): ApiResponse<out Any> {
        val operationId = databaseRepository.detopnameid(operationName) ?: return ApiResponse.Error(
            "Operation Id found", null
        )

        createLog("TAG_RADIO", "Operation ID -> Response $operationId Response")
        val cmdList = databaseRepository.commandidls1(operationId, dgps)
        createLog("TAG_RADIO", "Cmd List -> Response $cmdList Command List")
        if (cmdList.isEmpty()) {
            return ApiResponse.Error("Cannot found the CMD list", null)
        }

        val selectionConfig = databaseRepository.selectionidlist1(
            BaseConfigurationRepository.getSubString(
                cmdList.toString(),  1,  cmdList.toString().length - 1
            )
        )
        createLog("TAG_RADIO", "Response Selection $selectionConfig")
        if (selectionConfig.isEmpty()) {
            return ApiResponse.Error("Selection Id Not Found", null)
        }
        val cmdListString = BaseConfigurationRepository.getSubString(
            selectionConfig.toString(), 1, selectionConfig.toString().length - 1
        )

        createLog("TAG_RADIO", "Response Operation $cmdListString")

        val operation = databaseRepository.displayvaluelist1(
            cmdListString
        )

        createLog("TAG_RADIO", "Response Operation $operation")

        if (operation.isEmpty()) {
            return ApiResponse.Error("No Attribute Found to Take Information", null)
        }

        return ApiResponse.Success(operation)
    }


}