package com.apogee.geomaster.repository

import android.util.Log
import com.apogee.apilibrary.Interfaces.CustomCallback
import com.apogee.geomaster.instance.ModuleInstance
import com.apogee.geomaster.model.GetAllTblResponse
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.ApiUtils
import com.apogee.geomaster.utils.fromJson
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class LoginRepository(moduleInstance: ModuleInstance) :  CustomCallback {


    private val _data = MutableSharedFlow<ApiResponse<out Any>>()
    val data: SharedFlow<ApiResponse<out Any>>
        get() = _data


    private val api by lazy {
        moduleInstance.getApiInstance()
    }

    private val coroutine by lazy {
        moduleInstance.coroutineScope
    }

    fun getApiInfo() {
        coroutine.launch {
            api.postDataWithoutBody(
                this@LoginRepository,
                ApiUtils.POST_GET_TABLE_RECORDS.first,
                ApiUtils.POST_GET_TABLE_RECORDS.second
            )
            _data.emit(ApiResponse.Loading("Please Wait loading Data.."))
        }
    }


    override fun onResponse(p0: Call<*>?, res: Response<*>?, p2: Int) {
        coroutine.launch {
            val emitData = res?.let {
                val responseBody: ResponseBody = it.body() as ResponseBody
                if (res.isSuccessful) {
                    try {
                        val responseString = responseBody.string()
                        Log.d("API_RESPONSE", "onResponse: ${fromJson<GetAllTblResponse>(responseString)}")
                        ApiResponse.Success(fromJson<GetAllTblResponse>(responseString))
                    } catch (e: Throwable) {
                        Log.d("API_RESPONSE", "onResponse: " + e.message)
                        ApiResponse.Error(null, e)
                    }
                } else {
                    ApiResponse.Error(res.message(), null)
                }

            } ?: ApiResponse.Error(null, Throwable("Oops cannot find Response"))

            _data.emit(emitData)
        }
    }

    override fun onFailure(p0: Call<*>?, p1: Throwable?, p2: Int) {
        coroutine.launch {
            _data.emit(ApiResponse.Error(null, p1))
        }
    }

    fun cancel() {
        coroutine.cancel()
    }
}