package com.apogee.geomaster.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.model.User
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var user: User = User("", "")
    private val successMessage = "ApiHit was successful"
    private val errorMessage = "failed"


    private val _msgEvent = MutableStateFlow("")
    val msgEvent: StateFlow<String>
        get() = _msgEvent


    fun afterEmailTextChanged(s: CharSequence) {
        user.email = s.toString()
    }

    fun afterPasswordTextChanged(s: CharSequence) {
        user.password = s.toString()
    }

    fun onLoginClicked() {
        _msgEvent.value = (errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
