package com.apogee.geomaster.utils

import android.annotation.SuppressLint
import android.content.Context


class MyPreference(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: MyPreference? = null

        fun getInstance(context: Context): MyPreference {
            if (INSTANCE == null) {
                INSTANCE = MyPreference(context)
            }
            return INSTANCE!!
        }
    }

    private val preference = "GeoMasterPreference"
    private val sharedLoginPref by lazy {
        context.getSharedPreferences(preference, Context.MODE_PRIVATE)
    }



    fun putStringData(valkey: String, value: String) {
        val edit = sharedLoginPref.edit()
        edit.putString(valkey, value)
        edit.apply()
    }

    fun putBooleanData(valkey: String, value: Boolean) {
        val edit = sharedLoginPref.edit()
        edit.putBoolean(valkey, value)
        edit.apply()
    }


    fun getStringData(keyName: String): String {

        val getvalue = sharedLoginPref.getString(keyName, "").toString()
        return getvalue
    }

    fun getBooleanData(keyName: String): Boolean {
        val getvalue = sharedLoginPref.getBoolean(keyName, false)
        return getvalue
    }


}