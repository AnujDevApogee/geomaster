package com.apogee.geomaster.ui.configuration.addconfig

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateConfigurationFragmentBinding
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.google.android.material.transition.MaterialFadeThrough

class CreateConfigurationFragment : Fragment(R.layout.create_configuration_fragment) {

    private lateinit var binding: CreateConfigurationFragmentBinding

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
        binding = CreateConfigurationFragmentBinding.bind(view)
        displayActionBar("Survey Configuration", binding.actionLayout)
        binding.doneBtn.setOnClickListener {
            findNavController().safeNavigate(R.id.action_createConfigurationFragment_to_satelliteConfigurationFragment)
        }
    }

}