package com.apogee.geomaster.model

import com.apogee.geomaster.R

data class HomeScreenOption(
    val icon: Int,
    val title: String,
    val navId: Int
) {
    companion object {
        val list = listOf(
            HomeScreenOption(
                icon = R.drawable.ic_folder,
                title = "Project",
                navId = R.id.action_global_projectListFragment
            ),HomeScreenOption(
                icon = R.drawable.ic_device,
                title = "Device",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_survey,
                title = "Survey",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_setting,
                title = "Setting",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            )
        )
    }
}
