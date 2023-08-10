package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DeviceConfigurationAdaptor
import com.apogee.geomaster.databinding.DeviceConfigLayoutBinding
import com.apogee.geomaster.model.DeviceWorkMode
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox

class DeviceConfiguration : Fragment(R.layout.device_config_layout) {

    private lateinit var binding: DeviceConfigLayoutBinding

    private lateinit var adaptor: DeviceConfigurationAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DeviceConfigLayoutBinding.bind(view)
        displayActionBar("Device Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        setAdaptor()
        binding.doneBtn.setOnClickListener {
            activity?.setUpDialogBox("Modify Project",
                "Are you sure to modify the project or create a New One",
                "Update",
                "Create",
                success = {
                          findNavController().safeNavigate(R.id.action_deviceConfiguration_to_homeScreenMainFragment)
                },
                cancelListener = {

                })
        }
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