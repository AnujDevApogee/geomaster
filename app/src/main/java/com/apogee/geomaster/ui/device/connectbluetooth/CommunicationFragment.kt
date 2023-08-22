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

class CommunicationFragment : Fragment(R.layout.fragment_communication) {

    private lateinit var binding: FragmentCommunicationBinding
    val TAG = "CommunicationFragment"
    private lateinit var dbControl: DatabaseRepsoitory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommunicationBinding.bind(view)

    }

}

