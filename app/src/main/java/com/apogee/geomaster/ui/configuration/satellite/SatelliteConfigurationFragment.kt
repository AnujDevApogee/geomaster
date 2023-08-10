package com.apogee.geomaster.ui.configuration.satellite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.SatelliteScreenAdaptor
import com.apogee.geomaster.databinding.SatelliteConfigurationFragmentBinding
import com.apogee.geomaster.model.SatelliteModel
import com.apogee.geomaster.utils.displayActionBar

class SatelliteConfigurationFragment : Fragment(R.layout.satellite_configuration_fragment) {

    private lateinit var binding: SatelliteConfigurationFragmentBinding

    private lateinit var adaptor: SatelliteScreenAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SatelliteConfigurationFragmentBinding.bind(view)
        displayActionBar("Satellite Configuration", binding.actionLayout)
        setRecycleView()

    }

    private fun setRecycleView() {
        binding.recycleView.apply {
            this@SatelliteConfigurationFragment.adaptor = SatelliteScreenAdaptor {

            }
            adapter = this@SatelliteConfigurationFragment.adaptor
            this@SatelliteConfigurationFragment.adaptor.submitList(SatelliteModel.list)
        }
    }
}