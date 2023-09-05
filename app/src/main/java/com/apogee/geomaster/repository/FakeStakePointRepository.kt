package com.apogee.geomaster.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.Looper
import android.util.Log
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.utils.StakeHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
class FakeStakePointRepository(application: Application, private val data: MockStakePointImpl) {

    private val mFusedLocation by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }


    private val mLocationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).apply {
        }.build()
    }


    companion object {
        var altitude = 55.0068
    }

    fun fakeStakePoint() {
        for (i in 0..99999) {
            val obj = SurveyModel(
                i + 1,
                "point_${i + 1}",
                "Code_name_${i + 1}",
                easting = (28 + Math.random() * 5),
                northing = (77 + Math.random() * 5),
                43,
                56.0003324,
                "Prefix_Type",
                "RECORD_TYPE",
                "SERVER_TYPE"
            )
            data.receivePoint(obj)
        }
    }


    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            val mLastLocation = location.lastLocation
            Log.i(
                "MY_LAST_LOCATION_2",
                "onLocationResult: ${mLastLocation?.latitude} and ${mLastLocation?.longitude}"
            )
            if (mLastLocation != null) {
                val map = hashMapOf<String, Any>()
                map[StakeHelper.LONGITUDE] = mLastLocation.longitude
                map[StakeHelper.LATITUDE] = mLastLocation.latitude
                map[StakeHelper.ELEVATION] = mLastLocation.altitude
                map[StakeHelper.XAXIS] = (100..1000).random()
                map[StakeHelper.YAXIS] = (100..1000).random()
                map[StakeHelper.ZAXIS] = (100..1000).random()
                data.stakePoint(map)
            }
        }
    }


    fun getLocation() {
        mFusedLocation.requestLocationUpdates(
            mLocationRequest,
            locationCallBack,
            Looper.getMainLooper()
        )
    }


    fun disconnect() {
        mFusedLocation.removeLocationUpdates(locationCallBack)
    }

}