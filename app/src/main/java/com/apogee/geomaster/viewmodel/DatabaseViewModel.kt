package com.apogee.geomaster.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apogee.geomaster.repository.DatabaseRepsoitory
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val dbControl = DatabaseRepsoitory(application)

    private val db_response = MutableLiveData<Any?>()
    val dbResponse: LiveData<Any?>
        get() = db_response

    fun insertMiscellaneousConfigData(value: String) {
        viewModelScope.launch {
            val result = dbControl.insertMiscellaneousConfigData(value)
            db_response.postValue(
                if (result == 1) {
                    Pair("Data Inserted Successfully", true)
                } else {
                    Pair("Data Insertion Failed", false)
                }
            )

        }
    }

    fun init() {
        db_response.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}