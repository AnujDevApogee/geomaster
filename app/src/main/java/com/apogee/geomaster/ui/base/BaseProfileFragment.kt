@file:Suppress("UNCHECKED_CAST")

package com.apogee.geomaster.ui.base

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.apogee.basicble.Utils.MultiMap
import com.apogee.geomaster.R
import com.apogee.geomaster.bluetooth.DataResponseHandlingInterface
import com.apogee.geomaster.databinding.BaseProfileLayoutBinding
import com.apogee.geomaster.response_handling.ResponseHandling
import com.apogee.geomaster.response_handling.model.DBResponseModel
import com.apogee.geomaster.response_handling.model.ResponseHandlingModel
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.ui.connection.antenna.SetUpAntennaFragment
import com.apogee.geomaster.ui.device.connectbluetooth.BluetoothScanDeviceFragment
import com.apogee.geomaster.use_case.EditCommand
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getColorInt
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.BaseConfigurationViewModel
import com.apogee.geomaster.viewmodel.BleConnectionViewModel
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch

class BaseProfileFragment : Fragment(R.layout.base_profile_layout), DataResponseHandlingInterface {

    private lateinit var binding: BaseProfileLayoutBinding

    private val viewModel: BaseConfigurationViewModel by viewModels()

    private val bleConnectionViewModel: BleConnectionViewModel by activityViewModels()

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    private val responseHandling by lazy {
        ResponseHandling(requireActivity())
    }

    private val listOfCommand = mutableListOf<String>()
    private val responseList = mutableListOf<DBResponseModel>()

    private var list = mutableListOf<String>()

    private val deviceName by lazy {
        MyPreference.getInstance(requireActivity()).getStringData(Constants.DEVICE_NAME)
    }
    private val dgps by lazy {
        MyPreference.getInstance(requireActivity()).getStringData(Constants.DGPS_DEVICE_ID).toInt()
    }

    private var errorCount = 0
    private var currentIndex = 0

    companion object {
        var baseSetUp: Pair<String, Map<String, Any?>>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BaseProfileLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.gnss_base_profile),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        initial()

        getSetConfigResponses()
        getBaseConfigCmdResponses()
        getBleResponse()

        binding.setConnBtn.setOnClickListener {
            activity?.toastMsg("Connection click")
        }


        /*binding.deviceInfo.append(setHtmlBoldTxt("Make"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")
        binding.deviceInfo.append(setHtmlBoldTxt("Model"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")
        binding.deviceInfo.append(setHtmlBoldTxt("Device Name"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")*/


        if (SetUpAntennaFragment.measuredHeight != -1) {
            binding.setAntennaBtn.hide()
            binding.antennaCard.show()
            binding.antennaType.text = "Antenna ${SetUpAntennaFragment.measuredHeight}m"
            binding.setConnBtn.isCheckable = true
            binding.setConnBtn.isEnabled = true
            binding.setConnBtn.backgroundTintList =
                ColorStateList.valueOf(requireActivity().getColorInt(R.color.md_theme_dark_inversePrimary))
        }

        if (ConnectionFragment.connectionSelectionType != null) {
            binding.setAutoBtn.isCheckable = true
            binding.setAutoBtn.isEnabled = true
            binding.setManualBtn.isCheckable = true
            binding.setManualBtn.isEnabled = true
            binding.setAutoBtn.backgroundTintList =
                ColorStateList.valueOf(requireActivity().getColorInt(R.color.md_theme_dark_inversePrimary))
            binding.setManualBtn.backgroundTintList =
                ColorStateList.valueOf(requireActivity().getColorInt(R.color.md_theme_dark_inversePrimary))
            binding.setConnBtn.hide()
            binding.basicParamsCrd.show()
            binding.basicType.text = "Connection Setup via" +
                    " ${getBasicType(ConnectionFragment.connectionSelectionType?.first!!)}"

        }


        if (baseSetUp != null) {
            binding.setAutoBtn.hide()
            binding.setManualBtn.hide()
            // Start it
            viewModel.getBaseConfigCmd(
                baseSetUp!!.first,
                ConnectionFragment.connectionSelectionType?.first!!,
                dgps
            )
        }





        binding.setAntennaBtn.setOnClickListener {
            findNavController().safeNavigate(BaseProfileFragmentDirections.actionBaseProfileFragmentToSetUpAntennaFragment())
        }


        binding.setConnBtn.setOnClickListener {
            findNavController().safeNavigate(
                BaseProfileFragmentDirections.actionGlobalConnectionFragment(
                    list.toTypedArray()
                )
            )
        }


        binding.setAutoBtn.setOnClickListener {
            findNavController().safeNavigate(
                BaseProfileFragmentDirections.actionBaseProfileFragmentToAutoBaseFragment(
                    "Auto base"
                )
            )
        }

        binding.setManualBtn.setOnClickListener {
            findNavController().safeNavigate(
                BaseProfileFragmentDirections.actionBaseProfileFragmentToManualBaseFragment(
                    "Manual Base"
                )
            )
        }
    }

    private fun getBasicType(type: String): String {
        return when (type) {
            "WiFi" -> {
                type
            }

            "Radio" -> {
                type
            }

            "RS232" -> {
                "Radio"
            }

            "GSM" -> {
                type
            }

            else -> "NULL"
        }
    }

    private fun initial() {
        viewModel.setUpConfig(deviceName)
    }

    private fun getSetConfigResponses() {
        viewModel.baseConfigDataSetUp.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("BASE_SETUP", "Exception => ${it.exception?.message}")
                }

                is ApiResponse.Loading -> {
                    createLog("BASE_SETUP", "Loading ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("BASE_SETUP", "Success ${it.data}")
                    val response = it.data as Pair<*, *>
                    binding.deviceInfo.apply {
                        text = ""
                        append(setHtmlBoldTxt("Model"))
                        append("\t")
                        append(deviceName)
                        append("\n")
                        append(setHtmlBoldTxt("Device Name"))
                        append("\t")
                        append(deviceName)
                        // Renaming Info
                        append("\n")
                        append(setHtmlBoldTxt("Make"))
                        append("\t ")
                        append(response.first as String)
                        list.clear()
                        list.addAll(response.second as List<String>)
                    }
                }
            }
        }
    }

    private fun getBaseConfigCmdResponses() {
        viewModel.baseConfigCmd.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("BASE_SETUP_CMD", "Exception => ${it.exception?.message}")
                }

                is ApiResponse.Loading -> {
                    createLog("BASE_SETUP_CMD", "Loading ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("BASE_SETUP_CMD", "Success ${it.data}")

                    createLog("TAG_FULL_Info", "${ConnectionFragment.connectionSelectionType}")
                    createLog("TAG_FULL_Info", "$baseSetUp")
                    val data = (it.data as Pair<List<DBResponseModel>, List<String>>)
                    EditCommand.getEditCommand(
                        data.second,
                        ConnectionFragment.connectionSelectionType!!,
                        baseSetUp!!
                    ).run {
                        listOfCommand.clear()
                        listOfCommand.addAll(this)
                        responseList.clear()
                        responseList.addAll(data.first)
                        bleConnectionViewModel.writeToBle(listOfCommand.first())
                    }
                }
            }
        }
    }

    private fun getBleResponse() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bleConnectionViewModel.bleResponse.collect {
                    if (it != null) {
                        when (it) {
                            is BleResponse.OnConnected -> {
                                BluetoothScanDeviceFragment.BTConnected = true
                                createLog("ADD_GNSS_TEST","Ble Connected")
                            }


                            is BleResponse.OnConnectionClose -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: OnConnectClose " + it.message)
                            }

                            is BleResponse.OnDisconnected -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: OnDisconnected " + it.message)
                            }

                            is BleResponse.OnError -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: OnError " + it.message)
                            }

                            is BleResponse.OnLoading -> {
                                createLog("ADD_GNSS_TEST","OnLading ${it.message}")
                            }

                            is BleResponse.OnReconnect -> Log.d(
                                "ADD_GNSS_TEST",
                                " OnReconect getResponse: " + it.message
                            )

                            is BleResponse.OnResponseRead -> {
                                Log.d("ADD_GNSS_TEST", " ONResponseRead getResponse:GNSS ${it.response.data}")
                                if (responseList.isNotEmpty()) {
                                    responseHandling.validateResponse(
                                        it.response.data!!,
                                        7,
                                        responseList,
                                        this@BaseProfileFragment
                                    )
                                }
                            }


                            is BleResponse.OnResponseWrite -> Log.d(
                                "ADD_GNSS_TEST",
                                "onResponsesWrite getResponse: " + it.isMessageSend
                            )

                        }
                    }
                }
            }
        }
    }

    override fun gsaRecieveData(data: ArrayList<ResponseHandlingModel>) {
        TODO("Not yet implemented")
    }

    override fun gsvRecieveData(data: ArrayList<ResponseHandlingModel>) {
        TODO("Not yet implemented")
    }

    override fun fixResponseData(validate_res_map: MultiMap<String, String>) {
        TODO("Not yet implemented")
    }

    override fun ackRecieveData(status: Int) {
        when (status) {
            1 -> {
                try {
                    errorCount = 0
                    createLog("ADD_GNSS_TEST","${listOfCommand.first()} Acceoted")
                    listOfCommand.removeAt(0)
                    bleConnectionViewModel.writeToBle(listOfCommand.first())
                } catch (e: IndexOutOfBoundsException) {
                    if (listOfCommand.isEmpty()) {
                        showMessage("BaseConfigured")
                        createLog("ADD_GNSS_TEST","Base Confined")
                    }
                }
            }

            0 -> {
                if (errorCount <= 5 && listOfCommand.isNotEmpty()) {
                    createLog("ADD_GNSS_TEST","Re-sending ${listOfCommand.first()} $errorCount")
                    bleConnectionViewModel.writeToBle(listOfCommand.first())
                    errorCount += 1
                }
                if (errorCount > 5) {
                    showMessage("Failed to Configured!!")
                    createLog("ADD_GNSS_TEST","Failed to send config ${listOfCommand.first()}")
                    listOfCommand.clear()
                    errorCount = 0
                }
            }
        }
    }

}