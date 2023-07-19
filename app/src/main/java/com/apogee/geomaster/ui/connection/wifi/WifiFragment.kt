package com.apogee.geomaster.ui.connection.wifi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.WifiConnectionLayoutBinding

class WifiFragment : Fragment(R.layout.wifi_connection_layout) {

    private lateinit var binding: WifiConnectionLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WifiConnectionLayoutBinding.bind(view)

    }

}