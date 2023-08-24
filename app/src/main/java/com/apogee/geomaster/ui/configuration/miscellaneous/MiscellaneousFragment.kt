package com.apogee.geomaster.ui.configuration.miscellaneous


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.MiscellaneousLayoutBinding
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate

class MiscellaneousFragment : Fragment(R.layout.miscellaneous_layout) {

    private lateinit var binding: MiscellaneousLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MiscellaneousLayoutBinding.bind(view)
        displayActionBar("Miscellaneous Configuration", binding.actionLayout)
        binding.doneBtn.setOnClickListener {
            findNavController().safeNavigate(R.id.action_miscellaneousFragment_to_deviceConfiguration)
        }

    }

}
