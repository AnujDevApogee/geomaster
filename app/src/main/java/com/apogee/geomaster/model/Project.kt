package com.apogee.geomaster.model

data class Project(
    val title: String,
    val dataumName: String,
    val elevationKey: String,
) {
    companion object {
        val list = listOf(
            Project(
                "Sample Name 1",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 2",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 3",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 4",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 5",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 6",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 7",
                "120:230:123",
                "23",
            ),
            Project(
                "Sample Name 8",
                "120:230:123",
                "23",
            )

        )
    }
}
