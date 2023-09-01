package com.apogee.geomaster.model

import com.apogee.geomaster.R

data class HomeScreenOption(
    val icon: Int,
    val title: String,
    val navId: Int
) {
    companion object {
        val projectList = listOf(
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
            ), HomeScreenOption(
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
            ), HomeScreenOption(
                icon = R.drawable.settings,
                "Settings",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.wizard,
                "Work Mode",
                navId = -1
            ),HomeScreenOption(
                icon = R.drawable.ic_folder,
                "Configuration",
                navId = R.id.action_global_configurationFragment
            )
        )

        val deviceList = listOf(
            HomeScreenOption(
                icon = R.drawable.connection,
                "Connection",
                navId = R.id.action_global_bluetoothscandevicefragment

            ), HomeScreenOption(
                icon = R.drawable.rover,
                "Rover",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.base,
                "Base",
                navId = R.id.action_global_baseProfileFragment
            ), HomeScreenOption(
                icon = R.drawable.antenna,
                "Antenna",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.output,
                "Output",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.deviceinfo,
                "Device Info",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.position,
                "Position",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.registerr,
                "Register",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.staticc,
                "Static",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.hterminal,
                "H-Terminal",
                navId = -1
            )
        )

        val surveyScreenList = listOf(
            HomeScreenOption(
                icon = R.drawable.toposurvey,
                "Topo Survey",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.autosurvey,
                "Auto Survey",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.stakepointt,
                "Stake Point",
                navId = R.id.action_global_stakePointFragment
            ), HomeScreenOption(
                icon = R.drawable.stakelinee,
                "Stake Line",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ppk,
                "PPK",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.rtk_ppk,
                "RTK-PPK",
                navId = -1
            )
        )

        val toolsList = listOf(
            HomeScreenOption(
                icon = R.drawable.sitecaliberation,
                "Site Calibration",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.gridshift,
                "Grid Shift",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.cogo,
                "CaGo",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.ftp,
                "FTP",
                navId = -1
            ), HomeScreenOption(
                icon = R.drawable.email,
                "Email",
                navId = -1
            )
        )

    }
}
