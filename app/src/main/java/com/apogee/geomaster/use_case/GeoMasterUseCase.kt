package com.apogee.geomaster.use_case

import com.apogee.geomaster.model.GetAllTblResponse

class GeoMasterUseCase {

    fun getListOfName(data: GetAllTblResponse): List<String> {
        val mutableList = mutableListOf<String>()
        data.manufacturer.forEach {
            mutableList.add(it.name)
        }
        return mutableList
    }
}