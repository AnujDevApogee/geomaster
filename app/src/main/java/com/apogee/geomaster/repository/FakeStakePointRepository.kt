package com.apogee.geomaster.repository

import com.apogee.geomaster.model.SurveyModel

class FakeStakePointRepository {

    companion object {
        var latitude = 28.619558
        var longitude = 77.380608
        var altitude=55.0068
    }

    fun fakeStakePoint(data: MockStakePointImpl) {
        for (i in 0..9) {
            val obj = SurveyModel(
                i+1,
                "point_${i+1}",
                "Code_name_${i+1}",
                easting = (28 + Math.random() * 5),
                northing = (77 + Math.random() * 5),
                43,
                56.0003324,
                "Prefix_Type",
                "RECORD_TYPE",
                "SERVER_TYPE"
            )
            data.receivePoint(obj)
        }
    }

}