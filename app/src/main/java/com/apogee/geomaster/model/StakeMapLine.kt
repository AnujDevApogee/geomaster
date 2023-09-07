package com.apogee.geomaster.model

import com.apogee.geomaster.R


data class StakeMapLine(
    val id: Int
) {
    companion object {
        val list = listOf(
            StakeMapLine(
                id = R.drawable.setting_icon
            ), StakeMapLine(
                id = R.drawable.ic_easting
            ), StakeMapLine(
                id = R.drawable.ic_west
            ), StakeMapLine(
                id = R.drawable.ic_angle
            ), StakeMapLine(
                id = R.drawable.ic_setting
            ), StakeMapLine(
                id = R.drawable.ic_setting
            ), StakeMapLine(
                id = R.drawable.ic_west
            ), StakeMapLine(
                id = R.drawable.ic_angle
            )
        )

        val otherLayout = listOf(
            StakeMapLine(
                id = R.drawable.ic_add
            ), StakeMapLine(
                id = R.drawable.ic_easting
            ), StakeMapLine(
                id = R.drawable.ic_west
            ), StakeMapLine(
                id = R.drawable.ic_angle
            ), StakeMapLine(
                id = R.drawable.ic_add
            ), StakeMapLine(
                id = R.drawable.ic_easting
            ), StakeMapLine(
                id = R.drawable.ic_west
            ), StakeMapLine(
                id = R.drawable.ic_angle
            )
        )

    }
}
