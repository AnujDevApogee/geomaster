package com.apogee.geomaster.ui.configuration.addconfig

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateConfigurationFragmentBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.google.android.material.transition.MaterialFadeThrough

class CreateConfigurationFragment : Fragment(R.layout.create_configuration_fragment) {

    private lateinit var binding: CreateConfigurationFragmentBinding
    private lateinit var dbControl: DatabaseRepsoitory
    private var idList: HashMap<String, String> = HashMap()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbControl = DatabaseRepsoitory(this.requireContext())

        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateConfigurationFragmentBinding.bind(view)
        displayActionBar("Survey Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        binding.doneBtn.setOnClickListener {
            idList.put("datumName", "1")
            idList.put("distanceUnit", "1")
            idList.put("angleUnit", "1")
            idList.put("elevation", "1")
            idList.put("zoneData", "1")
            idList.put("config_name", "test")


            val result=dbControl.addConfigurationData(idList)
            if(result.equals("Data inserted successfully")){
                findNavController().safeNavigate(R.id.action_createConfigurationFragment_to_satelliteConfigurationFragment)
            }else{
                Toast.makeText(
                    this.requireContext(),
                    "Error While Insertion try Again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}