package com.apogee.geomaster.model

data class Project(
    var title: String,
    val dataumName: String,
    val projectionType: String,
    val zone: String
) {
    companion object {
        val list = listOf(
            Project(
                "ITEM_SAMPLE_1",
                "Dummy Data Name",
                "Project Type",
                "Zone Info"
            ),
            Project(
                "ITEM_SAMPLE_2",
                "Dummy Data Name",
                "Project Type",
                "Zone Info"
            ),
            Project(
                "ITEM_SAMPLE_3",
                "Dummy Data Name",
                "Project Type",
                "Zone Info"
            ),
            Project(
                "ITEM_SAMPLE_4",
                "Dummy Data Name",
                "Project Type",
                "Zone Info"
            ),
        )
    }
}
