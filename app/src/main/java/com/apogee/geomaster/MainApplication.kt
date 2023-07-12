package com.apogee.geomaster

import android.app.Application
import com.apogee.geomaster.instance.ModuleInstance

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ModuleInstance.getInstance().apply {
            getApiInstance()//Create API INSTANCE
        }
    }
}