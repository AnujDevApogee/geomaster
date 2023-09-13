package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.model.DynamicViewType
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
        val list = mutableListOf<DynamicViewType>()
        val operationId = databaseRepository.detopnameid(operationName) ?: return ApiResponse.Error(
            "Operation Id not found", null
        )

        createLog("TAG_RADIO", "Operation ID -> Response $operationId Response")
        val cmdList = databaseRepository.commandidls1(operationId, dgps)
        createLog("TAG_RADIO", "Cmd List -> Response $cmdList Command List")
        if (cmdList.isEmpty()) {
            return ApiResponse.Error("Cannot found the CMD list", null)
        }
        val cmdLs = BaseConfigurationRepository.getSubString(
            cmdList.toString(), 1, cmdList.toString().length - 1
        )
        val selectionConfig = databaseRepository.selectionidlist1(cmdLs)
        createLog("TAG_RADIO", "Response Selection $selectionConfig")

        val inputConfig = databaseRepository.inputlist(cmdLs)
        createLog("TAG_RADIO", "Response InputList EditText -> $inputConfig")

        if (selectionConfig.isNotEmpty()) {
            val cmdListString = BaseConfigurationRepository.getSubString(
                selectionConfig.toString(), 1, selectionConfig.toString().length - 1
            )
            createLog("TAG_RADIO", "Response Operation $cmdListString")

            val operation = databaseRepository.displayvaluelist1(
                cmdListString
            )

            list.addAll(getListOfView(operation))
        }

        if (inputConfig.isNotEmpty()) {
            val inputConfigLs = BaseConfigurationRepository.getSubString(
                inputConfig.toString(), 1, inputConfig.toString().length - 1
            )
            val editList = databaseRepository.inputparameterlistMAP(
                inputConfigLs
            )
            createLog("TAG_RADIO", "Edit_List FILE $editList")
            list.addAll(getEditTextView(editList))
        }

        return ApiResponse.Success(list.toList())
    }

    private fun getEditTextView(operation: Map<String, Pair<String, String>>): List<DynamicViewType> {
        val view = mutableListOf<DynamicViewType>()
        var indx=0
        operation.forEach {
            val obj=DynamicViewType.EditText(
                indx,
                it.key
            )
            indx+=1
            view.add(obj)
        }
        return view.toList()
    }

    private fun getListOfView(operation: Map<String, Map<String, String>>): List<DynamicViewType> {
        val view = mutableListOf<DynamicViewType>()
        var indx = 0
        operation.forEach {
            val obj = DynamicViewType.SpinnerData(
                indx,
                it.key,
                dataList = it.value.keys.toList(),
                valueList = it.value.values.toList(),
            )
            view.add(obj)
            indx += 1
        }
        return view.toList()
    }


}