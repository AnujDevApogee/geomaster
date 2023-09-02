package com.apogee.geomaster.repository

import com.apogee.geomaster.model.SurveyModel

interface MockStakePointImpl {

    fun receivePoint(surveyModel: SurveyModel)

    fun stakePoint(hashMap: HashMap<String, String>)
}