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

    private val _data =
        MutableStateFlow<ApiResponse<Pair<MutableList<SurveyModel>, MutableList<IGeoPoint>>>?>(null)
    val data: StateFlow<ApiResponse<Pair<MutableList<SurveyModel>, MutableList<IGeoPoint>>>?>
        get() = _data

    private val _currentData = MutableStateFlow<ApiResponse<HashMap<String, Any>>?>(null)
    val currentData: StateFlow<ApiResponse<HashMap<String, Any>>?>
        get() = _currentData


    private val repo = StakePointRepository(application)

    init {
        pointOnMap()
        currCoordinate()
    }

    private fun currCoordinate() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.data.buffer(Channel.UNLIMITED).collect {
                _data.value = it
            }
        }
    }

    private fun pointOnMap() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.currentData.buffer(Channel.UNLIMITED).collect {
                _currentData.value = it
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