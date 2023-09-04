package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.GetBluetoothConfigDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun getServiceId(deviceName: String): List<String>? {

        return getBluetoothConfigDataRepository.getServiceIds(deviceName)

    }
    /*    fun getModelId(device_name: String): List<String>? {

            return getBluetoothConfigDataRepository.getModelId(device_name)

        }*/
    fun getModelName(deviceName: String): List<String>? {

        return getBluetoothConfigDataRepository.getModelName(deviceName)

    }

    fun getCharacteristicId(serviceId: String): List<String>? {

        return getBluetoothConfigDataRepository.getCharacteristicIds(serviceId)

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
