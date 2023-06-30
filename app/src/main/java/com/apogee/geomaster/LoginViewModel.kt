package com.apogee.geomaster

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.apogee.geomaster.BR
import com.apogee.geomaster.Model.User
import com.apogee.geomaster.Repository.RepositoryClass

class LoginViewModel(email: String, password: String ,repositoryClass: RepositoryClass) : BaseObservable() {
    var user: User = User(email, password)
    private val successMessage = "ApiHit was successful"
    private val errorMessage = "failed"
    @get:Bindable
    var toastMessage: String? = null
        private set(value) {
            field = value
            notifyPropertyChanged(BR.toastMessage)
        }

    fun afterEmailTextChanged(s: CharSequence) {
        user.email = s.toString()
    }

    fun afterPasswordTextChanged(s: CharSequence) {
        user.password = s.toString()
    }

    fun onLoginClicked() {
        if (RepositoryClass().responseData.equals("")||RepositoryClass().responseData.equals(null)){

            Log.d("TAG", "Flow onLoginClicked:errorMessage ${RepositoryClass().responseData}")
            toastMessage = errorMessage
        }

        else{
            Log.d("TAG", "Flow onLoginClicked:successMessage ${RepositoryClass().responseData}")
            toastMessage = successMessage
        }
    }
}
