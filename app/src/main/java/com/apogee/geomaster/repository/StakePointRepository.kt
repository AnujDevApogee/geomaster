package com.apogee.geomaster.repository

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

class StakePointRepository : MockStakePointImpl {


    private val _data = MutableStateFlow<ApiResponse<MutableList<SurveyModel>>?>(null)
    val data: StateFlow<ApiResponse<MutableList<SurveyModel>>?>
        get() = _data

    private val _pointData = MutableStateFlow<ApiResponse<MutableList<IGeoPoint>>?>(null)
    val pointData: StateFlow<ApiResponse<MutableList<IGeoPoint>>?>
        get() = _pointData

    private val fakeStakePointRepository = FakeStakePointRepository()


    private val coroutineScope = CoroutineScope(Dispatchers.IO)


    fun getPoint() {
        fakeStakePointRepository.fakeStakePoint(this)
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
            _data.value = ApiResponse.Success(listNode)
            _pointData.value = ApiResponse.Success(points)
        }
    }

    override fun stakePoint(hashMap: HashMap<String, String>) {

    }
}