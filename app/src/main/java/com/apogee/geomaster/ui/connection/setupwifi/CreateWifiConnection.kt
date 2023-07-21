package com.apogee.geomaster.ui.connection.setupwifi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateWifiConnectionLayoutBinding
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.google.android.material.transition.MaterialFadeThrough

class CreateWifiConnection : Fragment(R.layout.create_wifi_connection_layout) {

    private lateinit var binding: CreateWifiConnectionLayoutBinding
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

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
        binding = CreateWifiConnectionLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.wift_setup),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )

    }


}