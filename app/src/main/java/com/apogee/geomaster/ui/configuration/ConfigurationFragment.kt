package com.apogee.geomaster.ui.configuration

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ProjectListAdaptor
import com.apogee.geomaster.databinding.ConfigurationFragmentLayoutBinding
import com.apogee.geomaster.model.ConfigSetup
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox
import com.google.android.material.transition.MaterialFadeThrough

class ConfigurationFragment : Fragment(R.layout.configuration_fragment_layout) {


    private lateinit var binding:ConfigurationFragmentLayoutBinding


    private val recycleAdaptorCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            Log.i("TAG", "onClickListener: itemclick_project")

            if (response is ConfigSetup){
                activity?.setUpDialogBox("Information","Continue with ${response.title}","Continue","Cancel", success = {
                    myPreference.putStringData("Last_Used",response.title)
                    Log.i(TAG, "onClickListener: LastUsed_saved -> ${myPreference.getStringData("Last_Used")}")
                    findNavController().navigate(R.id.action_projectListFragment_to_homeScreenMainFragment)
                }, cancelListener = {

                })

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConfigurationFragmentLayoutBinding.bind(view)
        displayActionBar("Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        binding.addProject.setOnClickListener {
            findNavController().safeNavigate(ConfigurationFragmentDirections.actionConfigurationFragmentToCreateConfigurationFragment())
        }
    }


    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            findNavController().navigate(R.id.action_projectListFragment_to_configurationFragment)
        }
    }

    private fun setUpRecycleView() {
        binding.recycleViewProject.apply {
            projectListAdaptor = ProjectListAdaptor(recycleAdaptorCallback)
            adapter = projectListAdaptor
        }
    }


}