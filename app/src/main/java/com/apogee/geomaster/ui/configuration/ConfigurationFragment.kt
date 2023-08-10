package com.apogee.geomaster.ui.configuration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ConfigurationFragmentLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.google.android.material.transition.MaterialFadeThrough

class ConfigurationFragment : Fragment(R.layout.configuration_fragment_layout) {


    private lateinit var binding:ConfigurationFragmentLayoutBinding

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


}