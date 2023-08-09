package com.apogee.geomaster.model

import com.apogee.geomaster.R

data class HomeScreenOption(
    val icon: Int,
    val title: String,
    val navId: Int
) {
    companion object {
        val projectList= listOf(
            HomeScreenOption(
                icon = R.drawable.project,
                "Project",
                navId = R.id.action_global_projectListFragment
            ),
            HomeScreenOption(
                icon = R.drawable.datum,
                "Datum",
                navId = -1
            ),
            HomeScreenOption(
                icon = R.drawable.element,
                "Data Log",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.codelist,
                "CodeList",
                navId = -1
            ),
            HomeScreenOption(
                icon = R.drawable.importt,
                "Import",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.exporrt,
                "Export",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.settings,
                "Settings",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.wizard,
                "Work Mode",
                navId = -1
            )
        )

        val deviceList= listOf(
            HomeScreenOption(
                icon = R.drawable.connection,
                "Connection",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.rover,
                "Rover",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.base,
                "Base",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.antenna,
                "Antenna",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.output,
                "Output",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.deviceinfo,
                "Device Info",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.position,
                "Position",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.registerr,
                "Register",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.staticc,
                "Static",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.hterminal,
                "H-Terminal",
                navId = -1
            )
        )

        val list = listOf(
            HomeScreenOption(
                icon = R.drawable.ic_folder,
                title = "Project",
                navId = R.id.action_global_projectListFragment
            ), HomeScreenOption(
                icon = R.drawable.ic_device,
                title = "Base",
                navId = R.id.action_global_baseProfileFragment
            ), HomeScreenOption(
                icon = R.drawable.rtk_ppk,
                title = "Rover",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ic_setting,
                title = "Setting",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ic_notifications,
                title = "Notify",
                navId = -1
            )
        )
    }
}
