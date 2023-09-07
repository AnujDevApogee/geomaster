package com.apogee.geomaster.ui.configuration.deviceconfig

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DeviceConfigurationAdaptor
import com.apogee.geomaster.databinding.DeviceConfigLayoutBinding
import com.apogee.geomaster.model.DeviceMode
import com.apogee.geomaster.model.SatelliteModel
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.setUpDialogBox

class DeviceConfiguration : Fragment(R.layout.device_config_layout) {

    private lateinit var binding: DeviceConfigLayoutBinding

    private lateinit var adaptor: DeviceConfigurationAdaptor
    private val args by navArgs<DeviceConfigurationArgs>()
    private lateinit var dbControl: DatabaseRepsoitory

    private lateinit var myPreference : MyPreference
    var WorkModeList: ArrayList<DeviceMode> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DeviceConfigLayoutBinding.bind(view)
        dbControl = DatabaseRepsoitory(requireContext())
        myPreference=MyPreference.getInstance(requireContext())


        displayActionBar("Device Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()

        val surveyConfigId = dbControl.getproject_configurationID(args.surveyConfigName)
        Log.d("TAG", "onViewCreated: surveyConfigIdDev --$surveyConfigId")

        setAdaptor()
        binding.doneBtn.setOnClickListener {
            activity?.setUpDialogBox("Modify Project",
                "Are you sure to modify the project or create a New One",
                "Update",
                "Create",
                success = {

       /*             val result =
                        dbControl.insertConfigMappingData("${args.surveyConfigName}Config,${surveyConfigId}")
                    Log.d("TAG", "onViewCreated:insertConfigMappingData $result")
                    val configMappId =
                        dbControl.getproject_configurationMappingID("${args.surveyConfigName}Config")
                    if (configMappId.equals("")) {
                        Log.d("TAG", "onViewCreated: $configMappId")
                    } else {
//                        val resultmapping = dbControl.insertSatelliteMappingDataasas(configMappId,args.satelliteDataValues)

//                        Log.d("TAG", "onViewCreated: resultConfigInsert--$resultmapping")
//                        if (resultmapping != 0) {
                            val saveproject =
                                dbControl.insertProjectValues("${args.surveyConfigName},$configMappId")
                            if(saveproject!=0){
                                myPreference.putStringData("Last_Used", args.surveyConfigName)
                                myPreference.putStringData("Last_Used_config","${args.surveyConfigName}Config")
//                                findNavController().safeNavigate(R.id.action_deviceConfiguration_to_homeScreenMainFragment)
                            }else{
                                Toast.makeText(requireContext(), "Project Creation Failed", Toast.LENGTH_SHORT).show()
                            }
//                        } else {
//                            Toast.makeText(requireContext(), "SatelliteMapping Failed", Toast.LENGTH_SHORT).show()
//                        }
                    }*/
                },
                cancelListener = {

                })
        }
    }

    private fun setAdaptor() {


        binding.recycleView.apply {
            this@DeviceConfiguration.adaptor = DeviceConfigurationAdaptor{ op, position ->
                Toast.makeText(requireContext(), "${op.type}", Toast.LENGTH_SHORT).show()

            }
            adapter = this@DeviceConfiguration.adaptor
            this@DeviceConfiguration.adaptor.submitList(DeviceMode.list)
        }
    }

}