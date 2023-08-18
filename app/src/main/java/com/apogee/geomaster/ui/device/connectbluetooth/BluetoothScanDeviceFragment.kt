package com.apogee.geomaster.ui.device.connectbluetooth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.BleDeviceAdaptor
import com.apogee.geomaster.databinding.FragmentCommunicationBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.PermissionUtils
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.showDeviceAdd
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.viewmodel.BleConnectionViewModel
import com.apogee.geomaster.viewmodel.BleGetConfigDataViewModel
import com.apogee.updatedblelibrary.BleDeviceScanner
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.google.android.material.transition.MaterialFadeThrough
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BluetoothScanDeviceFragment : Fragment(R.layout.fragment_communication) {

    private lateinit var binding: FragmentCommunicationBinding
    private lateinit var bleDeviceAdaptor: BleDeviceAdaptor
    private var bleDeviceScanner: BleDeviceScanner? = null
    private val bleConnectionViewModel: BleConnectionViewModel by viewModels()
    private val bleGetConfigDataViewModel: BleGetConfigDataViewModel by viewModels()
    var scanTime: Long = 3000


    // for Nordic
    private val descriptorId = "00002902-0000-1000-8000-00805f9b34fb"
    private var serviceId = "6e400001-b5a3-f393-e0a9-e50e24dcca9e"
    private var writeCharId = "6e400002-b5a3-f393-e0a9-e50e24dcca9e"
    private var readCharId = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough


    }


    @SuppressLint("NotifyDataSetChanged", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCommunicationBinding.bind(view)

        if (!bleGetConfigDataViewModel.getServiceId().isNullOrEmpty()) {

            serviceId = bleGetConfigDataViewModel.getServiceId()!!.first()


        } else if (!bleGetConfigDataViewModel.getCharacteristicId().isNullOrEmpty()) {
            bleGetConfigDataViewModel.getCharacteristicId()!!.forEach {


                if (it.contains("read")) {

                    readCharId = it.split(",".toRegex()).first()
                } else if (it.contains("write")) {

                    writeCharId = it.split(",".toRegex()).first()
                }


                Log.d(TAG, "onViewCreatedit: " + it)


            }
        }

        displayActionBar(
            "\t\t\tAdd Device ${getEmojiByUnicode(0x1F4F6)}",
            binding.actionLayout,
            -1,
            navIcon = -1
        )
        (activity as HomeScreen?)?.hideActionBar()


        getResponse()

        getObserverData()




        binding.recycleViewBle.apply {
            bleDeviceAdaptor = BleDeviceAdaptor {


                val deviceAddress = it.device.address
                val deviceName = it.device.name.split("_".toRegex()).first()
                Log.d(TAG, "onViewCreateddevice_id: " + deviceName)
                bleGetConfigDataViewModel.getConfigData(deviceName)


                bleConnectionViewModel.onConnect(
                    deviceAddress,
                    readCharId,
                    writeCharId,
                    serviceId,
                    descriptorId,
                )

            }
            adapter = bleDeviceAdaptor
        }

        binding.pbBle.isVisible = false
        binding.msgPb.hide()


        lifecycleScope.launch {
            delay(2000)
            PermissionX.init(requireActivity())
                .permissions(PermissionUtils.permissions)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            requireActivity(),
                            "All permissions are granted",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "These permissions are denied: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }


        bleDeviceScanner = BleDeviceScanner(requireActivity())


        bleDeviceScanner!!.startScanning(scanTime)

        binding.addDeviceManually.setOnClickListener {
            getDeviceSerialNumber()

        }

        binding.swipeRefresh.setOnRefreshListener {


            Log.i("BLE_INFO", "onViewCreated: ${bleDeviceScanner!!.scanResults}")
            bleDeviceAdaptor.notifyDataSetChanged()
            bleDeviceAdaptor.submitList(bleDeviceScanner!!.scanResults)

            binding.swipeRefresh.isRefreshing = false


        }


        bleConnectionViewModel.setupConnection()


    }

    private fun getObserverData() {

        lifecycleScope.launch {

            bleGetConfigDataViewModel.getBlutoothData.collect {

                if (it is String) {


                }

            }

        }


    }

    private fun getResponse() {

        lifecycleScope.launch {

            bleConnectionViewModel.bleResponse.collect {
                if (it != null) {
                    when (it) {
                        is BleResponse.OnConnected ->
                            binding.pbBle.isVisible = false


                        is BleResponse.OnConnectionClose -> Log.d(TAG, "getResponse: " + it.message)
                        is BleResponse.OnDisconnected -> Log.d(TAG, "getResponse: " + it.message)
                        is BleResponse.OnError -> Log.d(TAG, "getResponse: " + it.message)
                        is BleResponse.OnLoading ->
                            binding.pbBle.isVisible = true


                        is BleResponse.OnReconnect -> Log.d(TAG, "getResponse: " + it.message)
                        is BleResponse.OnResponseRead -> {
                            Log.d(
                                TAG,
                                "getResponse: " + it.response
                            )
                            findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)


                        }


                        is BleResponse.OnResponseWrite -> Log.d(
                            TAG,
                            "getResponse: " + it.isMessageSend
                        )

                    }
                }
            }

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