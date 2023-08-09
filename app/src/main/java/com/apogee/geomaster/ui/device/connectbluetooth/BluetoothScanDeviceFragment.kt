package com.apogee.geomaster.ui.device.connectbluetooth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentCommunicationBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.showDeviceAdd
import com.apogee.geomaster.utils.showMessage
import com.google.android.material.transition.MaterialFadeThrough

class BluetoothScanDeviceFragment : Fragment(R.layout.fragment_communication) {

    private lateinit var binding: FragmentCommunicationBinding


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
        binding = FragmentCommunicationBinding.bind(view)
        displayActionBar(
            "\t\t\tAdd Device ${getEmojiByUnicode(0x1F4F6)}",
            binding.actionLayout,
            -1,
            navIcon = -1
        )
        (activity as HomeScreen?)?.hideActionBar()
        binding.pbBle.isVisible = false
        binding.msgPb.hide()


        binding.addDeviceManually.setOnClickListener {
            getDeviceSerialNumber()
        }


    }

    private fun getDeviceSerialNumber() {
        showDeviceAdd(success = {
            showMessage(it)
            findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
        }, cancel = {

        })
    }


}