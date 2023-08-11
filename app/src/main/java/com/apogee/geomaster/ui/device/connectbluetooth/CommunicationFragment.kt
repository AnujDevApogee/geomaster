package com.apogee.geomaster.ui.device.connectbluetooth

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apogee.apilibrary.ApiCall
import com.apogee.apilibrary.Interfaces.CustomCallback
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateProjectsFragmentBinding
import com.apogee.geomaster.databinding.FragmentCommunicationBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class CommunicationFragment : Fragment(R.layout.fragment_communication), CustomCallback {

    private lateinit var binding: FragmentCommunicationBinding
    val TAG = "CommunicationFragment"
    private lateinit var dbControl: DatabaseRepsoitory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommunicationBinding.bind(view)
        dbControl = DatabaseRepsoitory(this.requireContext())
        Log.d(TAG, "onViewCreated: Bluetooth")

        ApiCall().postDataWithBody(
            "NAVIK200_1.1",
            this,
            "http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getBluetoothConfigurationData/",
            50
        )

    }

    override fun onResponse(p0: Call<*>?, response: Response<*>?, p2: Int) {
        val responseBody = response?.body() as ResponseBody?

        if (response!!.isSuccessful) {
            if (responseBody != null) {
                try {
                    val responseString = responseBody.string()
                    Log.d(TAG, "onResponse:Bluetooth $responseString")
                    dbControl.BluetoothConfigurationData(responseString)
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this.requireContext())
                    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                    editor.putString(Constants.BLUTOOTH_RESPONSE_STRING, responseString)
                    editor.apply()
                } catch (e: Exception) {
                    Log.d(TAG, "onResponse:Bluetooth Error ${e.message}")
                }
            }
        } else {
            val strOutput = response.toString()
        }

    }

    override fun onFailure(p0: Call<*>?, p1: Throwable?, p2: Int) {
        TODO("Not yet implemented")
    }
}

