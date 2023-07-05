package com.apogee.newgeo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.newgeo.R
import com.apogee.newgeo.databinding.DeviceLayoutFragmentBinding

class DeviceFragment :Fragment(R.layout.device_layout_fragment) {
    private lateinit var binding:DeviceLayoutFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= DeviceLayoutFragmentBinding.bind(view)

    }
}