package com.apogee.geomaster.ui.connection.setwifi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateWifiConnectionLayoutBinding

class CreateWifiConnection : Fragment(R.layout.create_wifi_connection_layout) {

    private lateinit var binding: CreateWifiConnectionLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CreateWifiConnectionLayoutBinding.bind(view)

    }


}