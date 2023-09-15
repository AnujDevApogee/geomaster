package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.BaseCommandRepository
import com.apogee.geomaster.repository.BaseConfigurationRepository
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.Serializable

class BaseConfigurationViewModel(application: Application) : AndroidViewModel(application) {


    private val _baseConfigDataSetUp = MutableLiveData<ApiResponse<out Serializable>>()
    val baseConfigDataSetUp: LiveData<ApiResponse<out Serializable>>
        get() = _baseConfigDataSetUp


    private val _baseConfigCmd = MutableLiveData<ApiResponse<out Any?>>()
    val baseConfigCmd: LiveData<ApiResponse<out Any?>>
        get() = _baseConfigCmd


    private val repo = BaseConfigurationRepository(application)

    private val baseRepo = BaseCommandRepository(application)


    fun setUpConfig(deviceName: String) {
        viewModelScope.launch {
            repo.setUpBaseConfig(deviceName).collect {
                _baseConfigDataSetUp.postValue(it)
            }
        }
    }


    fun getBaseConfigCmd(operationId: String, rtk: String, dgps: Int) {
        viewModelScope.launch {
            baseRepo.getCommandBle(operationId, rtk, dgps).collect {
                _baseConfigCmd.postValue(it)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}