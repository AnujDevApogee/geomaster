package com.apogee.geomaster.ui.device.connectbluetooth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentBluetoothScanDeviceBinding
import com.apogee.geomaster.databinding.FragmentCommunicationBinding

class BluetoothScanDeviceFragment : Fragment(R.layout.fragment_bluetooth_scan_device) {

    private lateinit var binding : FragmentBluetoothScanDeviceBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBluetoothScanDeviceBinding.bind(view)
    }

}