package com.example.stakemodual.utils

import com.apogee.geomaster.model.SurveyModel

interface MockStakePointImpl {

    fun receivePoint(surveyModel: SurveyModel)

    fun stakePoint(hashMap: HashMap<String, Any>)
}