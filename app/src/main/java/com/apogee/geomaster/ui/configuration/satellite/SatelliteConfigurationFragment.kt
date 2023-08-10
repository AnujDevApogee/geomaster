package com.apogee.geomaster.ui.configuration.satellite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.SatelliteConfigurationFragmentBinding
import com.apogee.geomaster.utils.displayActionBar

class SatelliteConfigurationFragment : Fragment(R.layout.satellite_configuration_fragment) {

    private lateinit var binding: SatelliteConfigurationFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SatelliteConfigurationFragmentBinding.bind(view)
        displayActionBar("Satellite Configuration",binding.actionLayout)

    }
}