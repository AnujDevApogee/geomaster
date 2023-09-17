package com.example.stakemodual.model

import com.example.stakemodual.R


data class StakeMapLine(
    val id: Int
) {
    companion object {
        val list = listOf(
            StakeMapLine(
                id = R.drawable.setting_icon
            ), StakeMapLine(
                id = R.drawable.ic_codelist
            ), StakeMapLine(
                id = R.drawable.ic_cogo
            ), StakeMapLine(
                id = R.drawable.current_location
            ), StakeMapLine(
                id = R.drawable.satellite
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
                id = R.drawable.ic_stakeout
            ), StakeMapLine(
                id = R.drawable.street_view
            ), StakeMapLine(
                id = R.drawable.zoomin
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
