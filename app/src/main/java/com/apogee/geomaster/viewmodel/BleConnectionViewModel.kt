package com.apogee.geomaster.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.model.User
import com.apogee.geomaster.repository.BleConnectionRepository
import com.apogee.updatedblelibrary.Utils.BleResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BleConnectionViewModel(application: Application) : AndroidViewModel(application) {

    var bleConnectionRepository: BleConnectionRepository = BleConnectionRepository(application)

    private val _bleResponse = MutableStateFlow<BleResponse?>(null)
    val bleResponse: StateFlow<BleResponse?>
        get() = _bleResponse


    init {
        onRead()
    }


    fun setupConnection() {

        bleConnectionRepository.setupConnection()

    }

     fun onConnect(deviceAddress: String,
                          readCharacteristic: String,
                          writeCharacteristic: String,
                          serviceUUIDId: String,
                          descriptorId: String) {

         viewModelScope.launch {

             bleConnectionRepository.onConnect(deviceAddress,readCharacteristic,writeCharacteristic,serviceUUIDId,descriptorId)

         }


    }



   private fun onRead() {

        viewModelScope.launch {

          bleConnectionRepository.bleResponse.collect {

              _bleResponse.value = it


          }

        }


    }



}
