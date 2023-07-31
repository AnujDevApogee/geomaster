package com.apogee.geomaster.utils

import android.annotation.SuppressLint
import android.content.Context

class GeoMasterSharedPreference(private val context: Context) {


        companion object {
            @SuppressLint("StaticFieldLeak")
            private var INSTANCE: GeoMasterSharedPreference? = null

            fun getInstance(context: Context): GeoMasterSharedPreference {
                if (INSTANCE == null) {
                    INSTANCE = GeoMasterSharedPreference(context)
                }
                return INSTANCE!!
            }
        }

        private val sharedPrefXML = "GeoMasterSharedPref"
        private val username = "UserName"
        private val pass = "Password"
        private val remember = "Remember"

        private val loginResponse = "Login_Response"
        private val sharedLoginPref by lazy {
            context.getSharedPreferences(sharedPrefXML, Context.MODE_PRIVATE)
        }

        fun saveUserNameAndPassword(username: String, pass: String, remember: Boolean) {
            val edit = sharedLoginPref.edit()
            edit.putString(this.username, username)
            edit.putString(this.pass, pass)
            edit.putBoolean(this.remember, remember)
            edit.apply()
        }




        fun getLoginCredential(): Pair<Pair<String, String>, Boolean> {
            return Pair(
                Pair(
                    sharedLoginPref.getString(username, "") ?: "",
                    sharedLoginPref.getString(pass, "") ?: ""
                ), sharedLoginPref.getBoolean(remember, false)
            )
        }


    }



