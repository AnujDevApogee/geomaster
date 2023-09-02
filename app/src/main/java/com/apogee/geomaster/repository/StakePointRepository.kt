package com.apogee.geomaster.repository

import android.app.Application
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.utils.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import java.util.Collections

class StakePointRepository(application: Application) : MockStakePointImpl {

    private val _data =
        MutableStateFlow<ApiResponse<Pair<MutableList<SurveyModel>, MutableList<IGeoPoint>>>?>(null)
    val data: StateFlow<ApiResponse<Pair<MutableList<SurveyModel>, MutableList<IGeoPoint>>>?>
        get() = _data

    private val _currentData = MutableStateFlow<ApiResponse<HashMap<String, Any>>?>(null)
    val currentData: StateFlow<ApiResponse<HashMap<String, Any>>?>
        get() = _currentData


    private val fakeStakePointRepository = FakeStakePointRepository(application, this)


    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun getPoint() {
        fakeStakePointRepository.fakeStakePoint()
        fakeStakePointRepository.getLocation()
    }

    private val listNode = mutableListOf<SurveyModel>()
    private val points: MutableList<IGeoPoint> = Collections.synchronizedList(ArrayList())

    override fun receivePoint(surveyModel: SurveyModel) {
        coroutineScope.launch {
            listNode.add(surveyModel)
            delay(100)
            points.add(
                LabelledGeoPoint(
                    surveyModel.easting,
                    surveyModel.northing,
                    surveyModel.pointName
                )
            )
            _data.value = ApiResponse.Success(Pair(listNode, points))
        }
    }

    override fun stakePoint(hashMap: HashMap<String, Any>) {
        coroutineScope.launch {

            _currentData.value = ApiResponse.Success(hashMap)
        }
    }
}