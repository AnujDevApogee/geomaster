package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.SetupConnectionRepository
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SetUpConnectionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo by lazy {
        SetupConnectionRepository(application)
    }


    private val _dataResponse = MutableLiveData<ApiResponse<out Any>>()
    val dataResponse: LiveData<ApiResponse<out Any>>
        get() = _dataResponse


    fun getInputRequiredParma(operationId: String, dgps: Int) {
        viewModelScope.launch {
            repo.getInputRequiredParma(operationId, dgps).collect {
                _dataResponse.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}