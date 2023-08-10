package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DeviceConfigurationAdaptor
import com.apogee.geomaster.databinding.DeviceConfigLayoutBinding
import com.apogee.geomaster.model.DeviceWorkMode
import com.apogee.geomaster.utils.displayActionBar

class DeviceConfiguration : Fragment(R.layout.device_config_layout) {

    private lateinit var binding: DeviceConfigLayoutBinding

    private lateinit var adaptor: DeviceConfigurationAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DeviceConfigLayoutBinding.bind(view)
        displayActionBar("Device Configuration",binding.actionLayout)
        setAdaptor()
    }

    private fun setAdaptor() {
        binding.recycleView.apply {
            this@DeviceConfiguration.adaptor= DeviceConfigurationAdaptor {

            }
            adapter=this@DeviceConfiguration.adaptor
            this@DeviceConfiguration.adaptor.submitList(DeviceWorkMode.list)
        }
    }

}