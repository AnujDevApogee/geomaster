package com.apogee.geomaster.model

import android.text.TextUtils
import android.util.Patterns
import com.apogee.geomaster.repository.LoginRepository

class User(var email: String, var password: String) {
    val isInputDataValid: Boolean
        get() = !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.length > 5
    val apiHit= LoginRepository().myApi()
}