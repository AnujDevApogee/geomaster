package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DeviceConfigurationAdaptor
import com.apogee.geomaster.databinding.DeviceConfigLayoutBinding
import com.apogee.geomaster.model.DeviceWorkMode
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox

class DeviceConfiguration : Fragment(R.layout.device_config_layout) {

    private lateinit var binding: DeviceConfigLayoutBinding

    private lateinit var adaptor: DeviceConfigurationAdaptor
    private val args by navArgs<DeviceConfigurationArgs>()
    private lateinit var dbControl: DatabaseRepsoitory

    private lateinit var myPreference : MyPreference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DeviceConfigLayoutBinding.bind(view)
        dbControl = DatabaseRepsoitory(requireContext())
        myPreference=MyPreference.getInstance(requireContext())


        displayActionBar("Device Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        Log.d(
            "TAG",
            "onViewCreated:argsDevice ---${args.surveyConfigName}---${args.satelliteConfigName} "
        )
        val surveyConfigId = dbControl.getproject_configurationID(args.surveyConfigName)
        val sateLiiteConfigId = dbControl.getSatelliteConfigurationID(args.satelliteConfigName)
        Log.d("TAG", "onViewCreated: surveyConfigIdDev --$surveyConfigId--$sateLiiteConfigId")

//        setAdaptor()
        binding.doneBtn.setOnClickListener {
            activity?.setUpDialogBox("Modify Project",
                "Are you sure to modify the project or create a New One",
                "Update",
                "Create",
                success = {
                    val result =
                        dbControl.insertConfigMappingData("${args.surveyConfigName}Config,${surveyConfigId},$sateLiiteConfigId")
                    Log.d("TAG", "onViewCreated:insertConfigMappingData $result")
                    val configMappId = dbControl.getproject_configurationMappingID("${args.surveyConfigName}Config")
                    if (configMappId.equals("")) {
                        Log.d("TAG", "onViewCreated: $configMappId")
                    } else {
                        val projectCreate=dbControl.insertProjectValues("${args.surveyConfigName},$configMappId")
                        if(projectCreate==1){
                            findNavController().safeNavigate(DeviceConfigurationDirections.actionDeviceConfigurationToHomeScreenMainFragment())
                            myPreference.putStringData("Last_Used", args.surveyConfigName)
                            myPreference.putStringData("Last_Used_config","${args.surveyConfigName}Config")

                            Toast.makeText(requireContext(), "Project Created Successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Project Creation  Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                cancelListener = {

                })
        }
    }

/*    private fun setAdaptor() {
        binding.recycleView.apply {
            this@DeviceConfiguration.adaptor = DeviceConfigurationAdaptor {
            }
            adapter = this@DeviceConfiguration.adaptor
            this@DeviceConfiguration.adaptor.submitList(DeviceWorkMode.list)
        }
    }*/

}