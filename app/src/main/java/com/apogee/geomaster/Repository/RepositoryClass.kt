package com.apogee.geomaster.Repository

import android.content.ContentValues
import android.content.Context
import android.util.Log

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class RepositoryClass{

    var responseData=""
fun myApi(){
    val RESULTCODE=500
//    ApiCall().postDataWithoutBody(this,"http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getAllTableRecords/",RESULTCODE)
}

//    override fun onResponse(call: Call<*>?, response: Response<*>?, requestCode: Int) {
//
//        val responseBody = response!!.body() as ResponseBody?
//
//        if (response.isSuccessful) {
//            if (responseBody != null) {
//                try {
//                    responseData= responseBody.string()
//                    Log.d("TAG", "Flow onResponse: $responseData")
//                } catch (e: java.lang.Exception) {
//                    Log.d("TAG", "onResponseException: " + e.message)
//                }
//            }
//        } else {
//            val strOutput = response.toString()
////            textViewReceivedDataResponse.text = "Response: $strOutput"
//            Log.d(ContentValues.TAG, "onResponse: $strOutput")
//        }
//    }
//
//    override fun onFailure(call: Call<*>?, t: Throwable?, requestCode: Int) {
//
//    }


}