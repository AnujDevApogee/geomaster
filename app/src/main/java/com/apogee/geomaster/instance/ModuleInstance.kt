package com.apogee.geomaster.instance

import com.apogee.apilibrary.ApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ModuleInstance {

    private var apiInstance: ApiCall? = null

    private val _coroutineScope = CoroutineScope(Dispatchers.IO)
    val coroutineScope: CoroutineScope
        get() = _coroutineScope

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