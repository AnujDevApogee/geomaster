package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.instance.ModuleInstance
import com.apogee.geomaster.model.GetAllTblResponse
import com.apogee.geomaster.repository.LoginRepository
import com.apogee.geomaster.use_case.GeoMasterUseCase
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo by lazy {
        LoginRepository(ModuleInstance.getInstance())
    }

    private val geoMasterUseCase = GeoMasterUseCase()


    private val _recordsTbl = MutableSharedFlow<ApiResponse<out Any?>>()
    val recordsTable: MutableSharedFlow<ApiResponse<out Any?>>
        get() = _recordsTbl

    init {
        listForChanges()
    }

    fun getTblRecords() {
        repo.getApiInfo()
    }

    private var job: Job?=null
    private fun listForChanges() {
        job?.cancel()
        job=viewModelScope.launch {
            repo.data.collect {
                if (it is ApiResponse.Success) {
                    val ls =
                        ApiResponse.Success(geoMasterUseCase.getListOfName(it.data as GetAllTblResponse))
                    _recordsTbl.emit(ls)
                } else {
                    if (it!=null)
                    _recordsTbl.emit(it)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}