package com.apogee.geomaster.ui.device.connectbluetooth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.MyPreference
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
    private var deviceName = ""
    private val bleConnectionViewModel: BleConnectionViewModel by activityViewModels()

    private val bleGetConfigDataViewModel: BleGetConfigDataViewModel by viewModels()
    var scanTime: Long = 3000

    // for Nordic
//    private val descriptorId = "00002902-0000-1000-8000-00805f9b34fb"
//    private var serviceId = "6e400001-b5a3-f393-e0a9-e50e24dcca9e"
//    private var writeCharId = "6e400002-b5a3-f393-e0a9-e50e24dcca9e"
//    private var readCharId = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"

    private val descriptorId = "00002902-0000-1000-8000-00805f9b34fb"
    private var serviceId = ""
    private var writeCharId = ""
    private var readCharId = ""
    var serviceIdForChar = ""
    private lateinit var dbControl: DatabaseRepsoitory
    var sharedPreferences: MyPreference? = null
    private var deviceAddress = ""
    companion object {
        var ChangeDevice = false
        var BTConnected=false
    }

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
        dbControl = DatabaseRepsoitory(requireContext())
        sharedPreferences = MyPreference.getInstance(requireContext())

        displayActionBar(
            "\t\t\tAdd Device ${getEmojiByUnicode(0x1F4F6)}",
            binding.actionLayout,
            -1,
            navIcon = -1
        )
        (activity as HomeScreen?)?.hideActionBar()


        binding.recycleViewBle.apply {
            bleDeviceAdaptor = BleDeviceAdaptor {

                deviceAddress = it.device.address
                deviceName = it.device.name.split("_".toRegex()).first()

                //   Log.d(TAG, "onViewCreated: "+bleGetConfigDataViewModel.getModelId(deviceName))
                if(bleGetConfigDataViewModel.getModelName(deviceName)!!.size==0)
                {
                    bleGetConfigDataViewModel.getConfigData(deviceName)
                }else
                {
                    getObserverData(deviceName)
                    fetchDetails(deviceName)
                }

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


    private fun fetchDetails(firstFourChars: String) {
        try {
            var dgpsIdRadio = ""
            val modelId = dbControl.getUserRegNo(firstFourChars)
            val getdevice = dbControl.getdeviceId(modelId!!)
            val deviceId = getdevice.split(",")[0]
            val headerLength = dbControl.getHeaderLength()
            Log.d(TAG, "fetchDetails: getDevice$getdevice --headerLength--$headerLength")
            val finishedModelType = dbControl.getMakeName(getdevice.split(",")[1])
            val moduleDeviceID = dbControl.getModuleFinishedId(deviceId)
            val joined = TextUtils.join(", ", moduleDeviceID)
            Log.d(
                "TAG",
                "joinedfetchDetails: " + deviceId + "\n" + joined + "\n" + finishedModelType
            )
            if (joined.contains("186")) {
                dgpsIdRadio = "186"
            }
            val deviceDetails = dbControl.getDeviceDetail(joined)
            val deviceModule = dbControl.getDeviceModule(joined)
            val modelIdList: ArrayList<String> = ArrayList()
            val bleTypeId = dbControl.getDeviceTypeeId("BLE")
            val dgpsTypeId = dbControl.getDeviceTypeeId("DGPS")
            val motherBoardTypeId = dbControl.getDeviceTypeeId("Motherboard")
            var deviceIds = ""
            var dgpsId = ""
            var motherBoardID = ""


            for (i in 0 until deviceDetails!!.size) {

                modelIdList.add(deviceDetails[i].split(",")[0])
                if (deviceDetails[i].split(",")[1] == bleTypeId) {
                    deviceIds = deviceDetails[i].split(",")[2]
                } else if (deviceDetails[i].split(",")[1] == dgpsTypeId) {
                    dgpsId = deviceDetails[i].split(",")[2]
                    Log.d("TAG", "fetchDetails:dgpsId " + dgpsId)
                }
                else if (deviceDetails[i].split(",")[1] == motherBoardTypeId) {
                    motherBoardID = deviceDetails[i].split(",")[2]
                    Log.d("TAG", "fetchDetails:motherBoardID " + motherBoardID)
                }
            }
            val joined1 = TextUtils.join(",", modelIdList)
            val modelDetails = dbControl.getModelDetail(joined1)
            Log.d(TAG, "fetchDetails: modelDetails--$modelDetails")

            var modelName = ""
            var profileName = ""
            for (i in 0 until modelDetails!!.size) {
                if (modelDetails[i].split(",")[1] == "7" +
                    ""
                ) {
                    modelName = modelDetails[i].split(",")[0]
                } else {
                    profileName = modelDetails[i].split(",")[0]
                }
            }
            val headerName=dbControl.getHeaderNameFromModelLogicMap(modelName)
            Log.d(
                TAG, "fetchDetails: finishedModelType--$finishedModelType \n" +
                        "modelName--$modelName \n" +
                        "profileName--$profileName \n" +
                        "deviceIds--$deviceIds \n" +
                        "dgpsId--$dgpsId \n" +
                        "dgpsIdRadio--$dgpsIdRadio \n" +
                        "deviceModule--${deviceModule}"
            )


            sharedPreferences!!.putStringData(Constants.MAKE, finishedModelType!!)
            sharedPreferences!!.putStringData(Constants.MODEL, modelName)
            sharedPreferences!!.putStringData(Constants.PROFILENAME, profileName)
            sharedPreferences!!.putStringData(Constants.DEVICE_ID, deviceIds)
            sharedPreferences!!.putStringData(Constants.DGPS_DEVICE_ID, dgpsId)
            sharedPreferences!!.putStringData(Constants.DGPS_DEVICE_ID_FOR_RADIO, dgpsIdRadio)
            sharedPreferences!!.putStringData(Constants.MODULE_DEVICE, deviceModule.toString())
            sharedPreferences!!.putStringData(Constants.DEVICE_NAME, firstFourChars)
            sharedPreferences!!.putStringData(Constants.HEADER_NAME, headerName!!)
            sharedPreferences!!.putStringData(Constants.HEADER_LENGTH, headerLength)
            sharedPreferences!!.putStringData(Constants.MOTHERBOARDID, motherBoardID)

        } catch (e: Exception) {
            Log.d(TAG, "fetchDetails: Exception --${e.message}")
        }


    }

    private fun getObserverData(deviceName: String) {



        if (!bleGetConfigDataViewModel.getServiceId(deviceName).isNullOrEmpty()) {

            var data = bleGetConfigDataViewModel.getServiceId(deviceName)!!.first().toString()


            Log.d(TAG, "getObserverData:bleData "+data)


            val dataArray: List<String> = data.split(",")

            serviceId = dataArray.get(0)

            serviceIdForChar = dataArray.get(1)


        }

        if (!bleGetConfigDataViewModel.getCharacteristicId(serviceIdForChar).isNullOrEmpty()) {

            bleGetConfigDataViewModel.getCharacteristicId(serviceIdForChar)!!.forEach {


                if (it.contains("read")) {

                    readCharId = it.split(",".toRegex()).first()

                    Log.d(TAG, "getObserverData: readCharId"+readCharId)

                } else if (it.contains("write")) {

                    writeCharId = it.split(",".toRegex()).first()

                    Log.d(TAG, "getObserverData: writeCharId"+writeCharId)

                }


                Log.d(TAG, "onViewCreatedit: " + it)


            }
        }

        lifecycleScope.launch {


            bleGetConfigDataViewModel.getBlutoothData.collect {

                Log.d(TAG, "getObserverData: "+deviceAddress+"=="+serviceId)
                bleConnectionViewModel.onConnect(
                    deviceAddress,
                    readCharId,
                    writeCharId,
                    serviceId,
                    descriptorId,
                )

                getResponse()


            }

        }


    }

    private fun getResponse() {

        lifecycleScope.launch {

            bleConnectionViewModel.bleResponse.collect {
                if (it != null) {
                    when (it) {
                        is BleResponse.OnConnected ->{
                            binding.pbBle.isVisible = false
                            BTConnected=true

                        }


                        is BleResponse.OnConnectionClose -> {
                            BTConnected=false
                            Log.d("ADD_DEVICE_TEST", "getResponse: " + it.message)
                        }
                        is BleResponse.OnDisconnected -> {
                            BTConnected=false
                            Log.d("ADD_DEVICE_TEST", "getResponse: " + it.message)
                        }
                        is BleResponse.OnError -> {
                            BTConnected=false
                            Log.d("ADD_DEVICE_TEST", "getResponse: " + it.message)
                        }
                        is BleResponse.OnLoading ->
                            binding.pbBle.isVisible = true


                        is BleResponse.OnReconnect -> Log.d("ADD_DEVICE_TEST", "getResponse: " + it.message)
                        is BleResponse.OnResponseRead -> {
                            if(ChangeDevice){
                                findNavController().safeNavigate(BluetoothScanDeviceFragmentDirections.actionGlobalGnssRoverProfileFragment())
                            }else{
                                Log.d("ADD_DEVICE_TEST", "getResponse: " + it.response)
                                findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
                            }
                        }


                        is BleResponse.OnResponseWrite -> Log.d(
                            "ADD_DEVICE_TEST",
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
            if(bleGetConfigDataViewModel.getModelName(deviceName)!!.size==0)
            {
                bleGetConfigDataViewModel.getConfigData(deviceName)
            }
            findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
        }, cancel = {

        })
    }


}