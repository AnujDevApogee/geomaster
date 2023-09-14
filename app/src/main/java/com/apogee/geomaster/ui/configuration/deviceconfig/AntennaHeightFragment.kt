package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentAntennaHeightBinding
import com.apogee.geomaster.databinding.FragmentGnssRoverProfileBinding


class AntennaHeightFragment : Fragment(R.layout.fragment_antenna_height) {

    private lateinit var binding: FragmentAntennaHeightBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAntennaHeightBinding.bind(view)





    }

}