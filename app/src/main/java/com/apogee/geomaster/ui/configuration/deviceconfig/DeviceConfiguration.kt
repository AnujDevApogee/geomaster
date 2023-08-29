package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DeviceConfigurationAdaptor
import com.apogee.geomaster.databinding.DeviceConfigLayoutBinding
import com.apogee.geomaster.model.DeviceWorkMode
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.configuration.miscellaneous.MiscellaneousFragmentArgs
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox

class DeviceConfiguration : Fragment(R.layout.device_config_layout) {

    private lateinit var binding: DeviceConfigLayoutBinding

    private lateinit var adaptor: DeviceConfigurationAdaptor
    private val args by navArgs<DeviceConfigurationArgs>()
    private lateinit var dbControl: DatabaseRepsoitory



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DeviceConfigLayoutBinding.bind(view)
        dbControl= DatabaseRepsoitory(requireContext())

        displayActionBar("Device Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        Log.d("TAG", "onViewCreated:argsDevice ${args.satelliteDataValues}---${args.surveyConfigName} ")
        val surveyConfigId=dbControl.getproject_configurationID(args.surveyConfigName)
        Log.d("TAG", "onViewCreated: surveyConfigIdDev --$surveyConfigId")

        setAdaptor()
        binding.doneBtn.setOnClickListener {
            activity?.setUpDialogBox("Modify Project",
                "Are you sure to modify the project or create a New One",
                "Update",
                "Create",
                success = {
                    val result = dbControl.insertConfigMappingData("${args.surveyConfigName}Config,${surveyConfigId}")

                    val resultmapping=dbControl.insertSatelliteMappingData()
                    Log.d("TAG", "onViewCreated: resultConfigInsert--$resultmapping")

//                          findNavController().safeNavigate(R.id.action_deviceConfiguration_to_homeScreenMainFragment)
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