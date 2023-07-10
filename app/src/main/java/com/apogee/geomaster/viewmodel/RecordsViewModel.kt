package com.apogee.geomaster.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.instance.ModuleInstance
import com.apogee.geomaster.repository.LoginRepository
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo by lazy {
        LoginRepository(ModuleInstance.getInstance())
    }

    private val _recordsTbl = MutableSharedFlow<ApiResponse<out Any?>>()
    val recordsTable: MutableSharedFlow<ApiResponse<out Any?>>
        get() = _recordsTbl

    init {
        listForChanges()
    }

    fun getTblRecords() {
        viewModelScope.launch {
            repo.getApiInfo()
        }
    }

    private fun listForChanges() {
        viewModelScope.launch {
            repo.data.collect {
                Log.i("LIVE", "Tsting_liva: $it")
                _recordsTbl.emit(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        repo.cancel()
    }

}