package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.BaseConfigurationRepository
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.Serializable

class BaseConfigurationViewModel(application: Application) : AndroidViewModel(application) {


    private val _baseConfigDataSetUp = MutableLiveData<ApiResponse<out Serializable>>()
    val baseConfigDataSetUp: LiveData<ApiResponse<out Serializable>>
        get() = _baseConfigDataSetUp

    private val repo = BaseConfigurationRepository(application)

    init {
        setUpConfig()
    }

    private fun setUpConfig() {
        viewModelScope.launch {
            repo.setUpBaseConfig().collect {
                _baseConfigDataSetUp.postValue(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}