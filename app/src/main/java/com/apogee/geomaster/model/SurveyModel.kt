package com.apogee.geomaster.model


data class SurveyModel(
    val id: Int,
    val pointName: String,
    val codeName: String,
    val easting: Double,
    val northing: Double,
    val zone: Int,
    val elevation: Double,
    val prefix: String,
    val record_type: String,
    val survey_type: String
){
    companion object{
        val list= listOf(
            SurveyModel(
                1,
                "Point 1",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                2,
                "Point 2",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                3,
                "Point 3",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                4,
                "Point 4",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                5,
                "Point 5",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                6,
                "Point 6",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                7,
                "Point 7",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),SurveyModel(
                8,
                "Point 8",
                "FGG",
                13.20424,
                11.20424,
                43,
                230.224324,
                "item on",
                "TYPE_LIST",
                "Survey Type"
            ),
        )
    }
}