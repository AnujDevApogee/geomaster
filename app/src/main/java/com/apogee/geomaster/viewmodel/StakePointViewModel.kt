package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.repository.StakePointRepository
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint

class StakePointViewModel(application: Application) : AndroidViewModel(application) {

    private val _data = MutableStateFlow<ApiResponse<MutableList<SurveyModel>>?>(null)
    val data: StateFlow<ApiResponse<MutableList<SurveyModel>>?>
        get() = _data


    private val _pointData = MutableStateFlow<ApiResponse<MutableList<IGeoPoint>>?>(null)
    val pointData: StateFlow<ApiResponse<MutableList<IGeoPoint>>?>
        get() = _pointData


    private val repo = StakePointRepository()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.data.buffer(Channel.UNLIMITED).collect {
                _data.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.pointData.buffer(Channel.UNLIMITED).collect {
                _pointData.value = it
            }
        }
    }

    fun getPoint() {
        repo.getPoint()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}