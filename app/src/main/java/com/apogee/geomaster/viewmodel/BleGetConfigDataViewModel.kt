package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.GetBluetoothConfigDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BleGetConfigDataViewModel(application: Application) : AndroidViewModel(application) {

    private var getBluetoothConfigDataRepository: GetBluetoothConfigDataRepository =
        GetBluetoothConfigDataRepository(application)

    private val _getBlutoothData = MutableStateFlow<Any?>(null)

    val getBlutoothData: MutableStateFlow<Any?>
        get() = _getBlutoothData

    fun getConfigData(deviceName: String) {

        getBluetoothConfigDataRepository.getConfigData(deviceName)

    }

    fun getServiceId(): List<String>? {

        return getBluetoothConfigDataRepository.getServiceIds()

    }
    fun getModelName(deviceName: String): List<String>? {

        return getBluetoothConfigDataRepository.getModelName(deviceName)

    }

    fun getCharacteristicId(): List<String>? {

        return getBluetoothConfigDataRepository.getCharacteristicIds()

    }

    init {

        observerDataListener()
    }

    private fun observerDataListener() {

        viewModelScope.launch {

            getBluetoothConfigDataRepository.getBlutoothData.collect {

                _getBlutoothData.value = it

            }

        }


    }


}
