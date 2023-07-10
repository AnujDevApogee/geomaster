package com.apogee.geomaster.instance

import com.apogee.apilibrary.ApiCall

class ModuleInstance {

    private var apiInstance: ApiCall? = null

    companion object {
        private var INSTANCE: ModuleInstance? = null
        fun getInstance(): ModuleInstance {
            if (INSTANCE == null) {
                INSTANCE = ModuleInstance()
            }
            return INSTANCE!!
        }
    }

    fun getApiInstance(): ApiCall {
        if (apiInstance == null) {
            apiInstance = ApiCall()
        }
        return apiInstance!!
    }


}