package com.apogee.geomaster.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.apogee.apilibrary.ApiCall
import com.apogee.apilibrary.Interfaces.CustomCallback
import com.apogee.geomaster.R
import com.apogee.geomaster.repository.DatabaseRepsoitory

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ApiService : Service(), CustomCallback {
    var count=0
    val TAG = "ApiService"
    private lateinit var dbControl:DatabaseRepsoitory


    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "my_channel_id"
    }

    override fun onCreate() {
        super.onCreate()
        try {
            dbControl=DatabaseRepsoitory(applicationContext)
            startForeground(NOTIFICATION_ID, createNotification())
        }catch (e:Exception){
            Log.i(TAG, "onCreate: ${e.message}")
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle your service logic here
        val REQUEST_CODE = 1
        ApiCall().postDataWithoutBody(
            this,
            "http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getCommonData/",
            REQUEST_CODE
        )
        return START_STICKY
        //"1.7"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("My Service")
            .setContentText("Running in the background")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)

        return notificationBuilder.build()
    }

    override fun onFailure(call: Call<*>?, t: Throwable?, requestCode: Int) {
        Log.d("TAG", "onFailure: " + t.toString())
    }

    override fun onResponse(call: Call<*>?, response: Response<*>?, requestCode: Int) {
        val responseBody = response?.body() as ResponseBody?

        if (response!!.isSuccessful) {
            if (responseBody != null) {
                try {
                    count++
                    val responseString = responseBody.string()
                    Log.d(TAG, "onResponse$count: $responseString")
                    dbControl.CommonApiTablesCreation(responseString)
                    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                    editor.putString(Constants.RESPONSE_STRING, responseString)
                    editor.apply()
                    stopService( Intent(this, ApiService::class.java))

                } catch (e: Exception) {
                    Log.d(TAG, "onResponse: ${e.message}")
                }
            }
        } else {
            val strOutput = response.toString()
        }
    }
}

