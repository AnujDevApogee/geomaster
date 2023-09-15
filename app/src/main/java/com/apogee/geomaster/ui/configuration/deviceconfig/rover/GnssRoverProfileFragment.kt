package com.apogee.geomaster.ui.configuration.deviceconfig.rover

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentGnssRoverProfileBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.ui.device.connectbluetooth.BluetoothScanDeviceFragment
import com.apogee.geomaster.utils.Conversion
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.BleConnectionViewModel
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.apogee.updatedblelibrary.Utils.BleResponseListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean


class GnssRoverProfileFragment : Fragment(R.layout.fragment_gnss_rover_profile),
    TextToSpeech.OnInitListener, BleResponseListener {
    val TAG = "GnssRoverProfileFragment"
    var isRTKPPK = false
    var modeWork = ""
    var mode = ""
    var opid = 0
    private val bleConnectionViewModel: BleConnectionViewModel by activityViewModels()

    var deviceInfotimerHandler = Handler(Looper.getMainLooper())
    private var mDeviceAddress: String? = null
    var services = false
    var mPlayer: MediaPlayer? = null
    var ispubx = false
    var deviceinfo = false

    //    var mBluetoothLeService: BluetoothLeService? = null
    var deviceName = ""
    private lateinit var dbControl: DatabaseRepsoitory
    var sharedPreferences: MyPreference? = null

    //    var dbTask = DatabaseOperation(this)
    var gnssdelay: ArrayList<String> = ArrayList()
    var radiodelay: ArrayList<String> = ArrayList()
    var gnnsFormatCommands: ArrayList<String> = ArrayList()
    var radioFormatCommands: ArrayList<String> = ArrayList()
    var gnsscommands: ArrayList<String> = ArrayList()
    var radiocommands: ArrayList<String> = ArrayList()
    var configTTs: TextToSpeech? = null
    var commandsfromlist: ArrayList<String> = ArrayList()
    var delaylist: ArrayList<String> = ArrayList()
    var commandsformatList: ArrayList<String> = ArrayList()
    var devicedetail: String = ""
    var newCommandList: ArrayList<String> = ArrayList()
    var datumcommands: ArrayList<String> = ArrayList()
    var datumdelay: ArrayList<String> = ArrayList()
    var datumFormatCommands: ArrayList<String> = ArrayList()
    var item = ""
    var map1: HashMap<String, String> = HashMap()
    var datum = ""
    var p_name = ""

    var pktno = 0
    private var isSpeak = true
    private var isInvalidSpeak = true
    private var isStanaloneSpeak = true
    private var isNotApplicable = true
    private var isRTKSpeak = true
    private var isRTKFSpeak = true
    private var isEstimatedSpeak = true
    private var isManualSpeak = true
    var totalnoofpkts = 0
    var datalist = ArrayList<String>()
    var gnggaenable = false
    var payloadfinal: String? = null
    var StatusData: String? = null
    var correction = ""
    var isBtConnected = false
    var isAnteenahUp = true
    var isCdParameters = true
    var isdiscnetHde = true

    //    private val newline = TextUtil.newline_crlf
    var issuccess = false
    var PDACorrection = false
    var counter = 0
    var commandCounter = 0
    var curpktNo = 0
    var isAlertFirst = true
    var msgss = ""

    //    var txtList = MyArrayList()
//    var hexList = MyArrayList()
    var timerHandler = Handler()
    var firstCommandResponse = ""
    var isRoverAllow = false
    var modelName = ""
    var responseCount = 0
    var showFirstTime = true
    var cancelWrite = false
    var isBtConnectedSuccessfully = false

    //    lateinit var progressDialog: com.marwaeltayeb.progressdialog.ProgressDialog
    var isClickEnable = false
    var textString = StringBuilder()
    var occupationTime = ""
//    lateinit var preferenceStore: PreferenceStore

    var progressCount = 0
    var isRadio = false

    var radiotimerHandler = Handler(Looper.getMainLooper())
    var radioMsg = ""
    var dialogSuccess = true
    var isFirstCallback = true
    var radioCount = 0
    var isSocketConnected = false
    var device_id = 0
    var dgps_id = 0


    companion object {
        var GPGGA_GNGGA = ""
        var PDATIME = ""
        var PDA = false
        var socket: Socket? = null
        var pdaStatus = ""
        var mConnected = false
        var stopThread = false
        var startTime = 0
        var roverMapProfile: HashMap<String, String> = HashMap()
        var radioMapProfile: HashMap<String, String> = HashMap()
        var externalradioMapProfile: HashMap<String, String> = HashMap()
        var parameterList: HashMap<String, String> = HashMap()
        var wifiMapProfile: HashMap<String, String> = HashMap()
        var pdaMapProfile: HashMap<String, String> = HashMap()
        var dataInputStreamPDA: DataInputStream? = null
        var dataOutputStreamPDA: DataOutputStream? = null


    }

    private val workingPDA = AtomicBoolean(true)
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null


    var IP = ""
    var Port = ""
    var dataRequest = ""
    var MountPoint = ""
    var usernameBase64 = ""
    var inputParam = ""
    var isFirstTimeSocket = true
    lateinit var pdaThread: Thread
    var srvrrqsttime = 0
    var pktintrvltime = 0
    var pktttszzzzzz = 0
    var temp_device_name = ""
    var satnum = "0"


    private lateinit var binding: FragmentGnssRoverProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbControl = DatabaseRepsoitory(requireContext())
        sharedPreferences = MyPreference.getInstance(requireContext())
        binding = FragmentGnssRoverProfileBinding.bind(view)
      //  fetchDetails("NAVIK200-1.1")

        dgps_id = sharedPreferences!!.getStringData(Constants.DGPS_DEVICE_ID).toInt()
        device_id = sharedPreferences!!.getStringData(Constants.DEVICE_ID).toInt()
        deviceName = sharedPreferences!!.getStringData(Constants.DEVICE_NAME)
        if (BluetoothScanDeviceFragment.BTConnected) {
            updateConnectionState(R.string.connected)
//            bleConnectionViewModel.setupConnection()
            getResponse()
        } else {
            updateConnectionState(R.string.disconnected)
        }

        loadLayout()

        if (isRTKPPK) {
            binding.triggerPoint.visibility = View.VISIBLE
            opid = dbControl.getOperationId(getString(R.string.rtk_ppk))
            Log.d(TAG, "onViewCreated:isRTKPPK--IF $opid")
            deviceInfotimerHandler.postDelayed(deviceInfoRunnable, 1000)

        } else {
            Log.d(TAG, "onViewCreated:isRTKPPK--Else ")
            opid = dbControl.getOperationId(getString(R.string.rover))
        }
        getcommandforparsing(opid, 0)

//        val datumId = dbControl.getOperationId(datum)
        val datumId = dbControl.getOperationId("WGS84")
        datumdelay = dbControl.delaylist(datumId, dgps_id)
        datumcommands = dbControl.commandforparsinglist(datumId, dgps_id)
        datumFormatCommands = dbControl.commandformatparsinglist(datumId, dgps_id)
        Log.d(
            TAG,
            "onViewCreated: datumId--$datumId \n--datumdelay--$datumdelay \n--datumcommands--$datumcommands\n datumFormatCommands--$datumFormatCommands"
        )

        onSetContentView()
        OnClickEvent()


    }

    private fun loadLayout() {
        binding.comupcard.visibility = View.VISIBLE
        binding.tvDeviceName.text = deviceName
        binding.tvConnection.text = deviceName
        binding.btCorrection.setBackgroundResource(R.drawable.buttondesign)
        binding.btCorrection.isClickable = true
        binding.btCorrection.isFocusable = true
        binding.btCorrection.isEnabled = true
        setDeviceInfo()
    }


    override fun onInit(p0: Int) {
    }

    private fun updateConnectionState(resourceId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            if (resourceId == R.string.connected) {
                binding.btCommunication.visibility = View.GONE
                binding.comupcard.visibility = View.VISIBLE
                binding.tvDeviceName.text = deviceName
                binding.tvConnection.text = deviceName
                binding.btCorrection.setBackgroundResource(R.drawable.buttondesign)
                binding.btCorrection.isClickable = true
                binding.btCorrection.isFocusable = true
                binding.btCorrection.isEnabled = true
            } else {
                binding.btCommunication.visibility = View.VISIBLE
                binding.comupcard.visibility = View.GONE
                binding.gnsscommunication.visibility = View.GONE
                binding.btCorrection.setBackgroundResource(R.drawable.buttondesign1)
            }

        }
    }


    override fun onResponse(res: BleResponse) {
        if (res != null) {
            when (res) {
                is BleResponse.OnConnected -> {}
//                         binding.pbBle.isVisible = false


                is BleResponse.OnConnectionClose -> Log.d(
                    ContentValues.TAG,
                    "getResponse: " + res.message
                )

                is BleResponse.OnDisconnected -> Log.d(
                    ContentValues.TAG,
                    "getResponse: " + res.message
                )

                is BleResponse.OnError -> Log.d(ContentValues.TAG, "getResponse: " + res.message)
                is BleResponse.OnLoading -> {

                }
//                         binding.pbBle.isVisible = true


                is BleResponse.OnReconnect -> Log.d(
                    ContentValues.TAG,
                    "getResponse: " + res.message
                )

                is BleResponse.OnResponseRead -> {
                    Log.d(ContentValues.TAG, "getResponse: " + res.response)
//                         findNavController().safeNavigate(R.id.action_bluetoothscandevicefragment_to_homeScreenMainFragment)
                }

                is BleResponse.OnResponseWrite -> Log.d(
                    ContentValues.TAG,
                    "getResponse: " + res.isMessageSend
                )
            }
        }


    }

    fun onSetContentView() {
        if (isRTKPPK) {
            binding.tvInitTime.visibility = View.VISIBLE
            binding.etInitTime.visibility = View.VISIBLE
            binding.tvOccTime.visibility = View.VISIBLE
            binding.fldropdwn.visibility = View.VISIBLE

            binding.etInitTime.isEnabled = false
        } else {
            binding.tvInitTime.visibility = View.GONE
            binding.etInitTime.visibility = View.GONE
            binding.tvOccTime.visibility = View.GONE
            binding.fldropdwn.visibility = View.GONE
        }
    }


    fun OnClickEvent() {

        binding.anteenadown.setOnClickListener {
            isAnteenahUp = true
            binding.anteenadown.visibility = View.GONE
            binding.anteenaup.visibility = View.VISIBLE
        }

        binding.anteenaup.setOnClickListener {
            isAnteenahUp = false
            binding.anteenadown.visibility = View.VISIBLE
            binding.anteenaup.visibility = View.GONE
        }

        binding.ibantennahdown.setOnClickListener {

//         findNavController().safeNavigate(R.layout.fragment_antenna_height)

            binding.ibantennahdown.isClickable = false
        }

        binding.cdParameters.setOnClickListener {
            isCdParameters = false
            binding.cdParameters.visibility = View.GONE
            binding.cdParametersdown.visibility = View.VISIBLE
        }

        binding.cdParametersdown.setOnClickListener {
            isCdParameters = true
            binding.cdParameters.visibility = View.VISIBLE
            binding.cdParametersdown.visibility = View.GONE
        }

        binding.done.setOnClickListener {
            if (satnum.isNotEmpty()) {
                if (!isClickEnable) {

                    if (datum == "" || p_name == "") {
                        requireActivity().toastMsg(getString(R.string.please_select_project_first))
                    } else if (roverMapProfile.size == 0 && radioMapProfile.size == 0 && externalradioMapProfile.size == 0 && wifiMapProfile.size == 0 && pdaMapProfile.size == 0) {
                        requireActivity().toastMsg(getString(R.string.please_set_up_correction_first))
                    } else if (parameterList.size == 0) {
                        requireActivity().toastMsg(getString(R.string.please_set_up_parameters_first))
                    } else if (devicedetail.isEmpty() && devicedetail.isEmpty()) {
                        requireActivity().toastMsg(getString(R.string.device_detail_is_empty))
                    } else if (binding.etInitTime.text.toString().trim().isEmpty() && isRTKPPK) {
                        requireActivity().toastMsg(getString(R.string.please_enter_initial_time))
                    } else if (occupationTime.isEmpty() && isRTKPPK) {
                        requireActivity().toastMsg(getString(R.string.please_enter_occupation_time))
                    } else if (binding.etInitTime.text.toString().trim()
                            .toInt() < 300 && isRTKPPK
                    ) {
                        requireActivity().toastMsg(getString(R.string.please_enter_initial_time_greater_than_5_min))
                    } else {
                        isClickEnable = true
                        isFirstCallback = true
                        dialogSuccess = true
                        try {
                            val id = dbControl.getidDataSource()
                            var dataSourceId = id.toInt()
                            dataSourceId++
                            Log.d(TAG, "OnClickEvent_map1: ${map1.keys} \n ${map1.values}")

                            if (map1.keys.contains("IP")) {
                                var ip = map1["IP"]!!
                                IP = Conversion(requireContext()).hextToString(ip).toString()
                            }

                            if (map1.keys.contains("Port")) {
                                var port = map1["Port"]!!
                                Port = Conversion(requireContext()).hextToString(port).toString()
                            }
                            if (map1.keys.contains("Mount-Point")) {
                                MountPoint =
                                    Conversion(requireContext()).hextToString(map1["Mount-Point"]!!)
                                        .toString()
                                var userName =
                                    Conversion(requireContext()).hextToString(map1["Username"]!!)
                                        .toString()
                                var ntripPassword =
                                    Conversion(requireContext()).hextToString(map1["NTRIP Password"]!!)
                                        .toString()
                                usernameBase64 =
                                    Conversion(requireContext()).stringToBase64(userName.trim() + ":" + ntripPassword.trim())
                            }

                            Log.d(
                                TAG,
                                "OnClickEvent AllValues: $IP \n $Port \n $MountPoint \n $usernameBase64 "
                            )

                            for ((key, value) in map1) {
                                var operation = ""
                                if (radioMapProfile.size > 0) {
                                    operation = getString(R.string.radio_rover)
                                } else if (externalradioMapProfile.size > 0) {
                                    operation = getString(R.string.radio_external)
                                } else if (roverMapProfile.size > 0) {
                                    operation = getString(R.string.gsm)
                                } else if (wifiMapProfile.size > 0) {
                                    operation = getString(R.string.wifi)
                                } else if (pdaMapProfile.size > 0) {
                                    Log.d(TAG, "OnClickEvent_pdaMapProfile: " + pdaMapProfile)
                                    operation = getString(R.string.pda)
                                }

                                dbControl.insertdataSorcestable(
                                    getString(R.string.rover),
                                    key,
                                    value,
                                    dataSourceId.toString(),
                                    operation,
                                    ""
                                )
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                        counter = 0
                        commandCounter = 0
                        responseCount = 0
//                        dddialog = null
                        cancelWrite = false
                        // connectTcp(100, 1500, 200)
                        send()
                    }
                }
            } else {
                requireActivity().toastMsg("Satellite not unavailable Please wait")
            }
        }

        binding.tvDisconnect.setOnClickListener {

            sharedPreferences!!.putStringData(Constants.DEVICE_NAME, "")
            sharedPreferences!!.putStringData(Constants.DEVICE_ADDRESSS, "")
            BluetoothScanDeviceFragment.BTConnected = false
//            bleConnectionViewModel.

            //      Utils.isBluetoothConnected = false
        }

        binding.btAntennah.setOnClickListener {
//            val intent = Intent(this@GNSSRoverProfile, AnteenaHeight::class.java)
//            startActivity(intent)
//            binding.btAntennah.isClickable = false
        }

//        binding.btParameters.setOnClickListener {
//            val intent = Intent(this@GNSSRoverProfile, BasicParameters::class.java)
//            if(deviceName.contains(resources.getString(R.string.navik300)) && externalradioMapProfile.size>0) {
//                intent.putExtra(Constants.RADIO_TYPE, resources.getString(R.string.radio_external))
//            }
//            startActivity(intent)
//            binding.btParameters.isClickable = false
//        }

        binding.gnsscommunication.setOnClickListener {
            isdiscnetHde = true
            binding.gnsscommunication.visibility = View.GONE
            binding.comupcard.visibility = View.VISIBLE
        }

        binding.comupcard.setOnClickListener {
            isdiscnetHde = false
            binding.comupcard.visibility = View.GONE
            binding.gnsscommunication.visibility = View.VISIBLE
        }
        binding.btCommunication.setOnClickListener {
            BluetoothScanDeviceFragment.ChangeDevice = true
            findNavController().safeNavigate(GnssRoverProfileFragmentDirections.actionGlobalBluetoothscandevicefragment())
            binding.btCommunication.isClickable = false
        }

        binding.btCorrection.setOnClickListener {
/*            if (modeWork == "Raw Log") {
                requireActivity().toastMsg(
                    "You can't Confugured Rover with Static, Please wait to finish static first."
                )
            } else {*/
            roverMapProfile.clear()
            radioMapProfile.clear()
            externalradioMapProfile.clear()
            wifiMapProfile.clear()
            pdaMapProfile.clear()
            findNavController().safeNavigate(
                GnssRoverProfileFragmentDirections.actionGnssRoverProfileFragmentToCorrectionFragment(
                    getString(R.string.rover)
                )
            )

//                val intent = Intent(this@GNSSRoverProfile, Correction::class.java)
//                intent.putExtra(Constants.GNSSMODULENAME, getString(R.string.rover))
//                startActivity(intent)
            binding.btCorrection.isClickable = false
//            }

        }

        binding.spindrop.setOnTouchListener { _, _ -> true }

        binding.spindrop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item: String = adapterView.getItemAtPosition(position).toString()
                if (item != "Select") {

                    occupationTime = item

                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

//        binding.triggerPoint.setOnClickListener {
//            if (binding.etInitTime.text.toString().trim().isEmpty()) {
//                Utils().setToast("Please Enter Initial Time", this)
//            } else if (occupationTime.isEmpty()) {
//                Utils().setToast("Please Enter Occupation Time", this)
//            } else if (binding.etInitTime.text.toString().trim().toInt() < 300) {
//                Utils().setToast("Please Enter Initial Time greater than 5 min.", this)
//            } else {
//                Utils.isFileWrite = true
//                if (Utils.isBTConnected) {
//                    try {
//                        val str = "Raw On"
//                        val msgs = (str + newline).toByteArray()
//                        Utils.service!!.write(msgs)
//                    } catch (ex: Exception) {
//
//                    }
//                }
//                val initialTime = binding.etInitTime.text.toString().trim()
//                val finalInitialTime = initialTime.toLong() * 1000
//                // Utils().checkInitailTime(finalInitialTime)
//                val intent = Intent(this, TopoSurveyActivity::class.java)
//                intent.putExtra(Constants.ISFROMPPK, "isFromPPkRTk")
//                intent.putExtra(Constants.INITIALTIME, finalInitialTime)
//                intent.putExtra(Constants.OCCUPATIONTIME, occupationTime)
//                startActivity(intent)
//                finish()
//            }
//        }

    }


    var deviceInfoCounter = 0


    var deviceInfoRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isRTKPPK) {
                deviceInfoCounter++

                if (modeWork == "PPK" || modeWork == "$mode + PPK") {
                    binding.done.isEnabled = false
                    binding.done.isClickable = false
                    binding.done.isFocusable = false
                    binding.done.setBackgroundResource(R.drawable.buttondesign1)
                    deviceInfotimerHandler.removeCallbacks(this)
                } else if (modeWork != "PPK" || modeWork != "$mode + PPK") {
                    binding.triggerPoint.isEnabled = false
                    binding.triggerPoint.isClickable = false
                    binding.triggerPoint.isFocusable = false
                    binding.triggerPoint.setBackgroundResource(R.drawable.buttondesign1)
                    deviceInfotimerHandler.removeCallbacks(this)
                }

                if (deviceInfoCounter < 6) {
                    deviceInfotimerHandler.postDelayed(this, 1000)
                }
            }
        }
    }


    private fun fetchDetails(firstFourChars: String) {
        try {
            var dgpsIdRadio = ""
            val modelId = dbControl.getUserRegNo(firstFourChars)
            val getdevice = dbControl.getdeviceId(modelId!!)
            val deviceId = getdevice!!.split(",")[0]
            val finishedModelType = dbControl.getMakeName(getdevice!!.split(",")[1])
            val moduleDeviceID = dbControl.getModuleFinishedId(deviceId)
            val joined = TextUtils.join(", ", moduleDeviceID!!)
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
            var deviceIds = ""
            var dgpsId = ""


            for (i in 0 until deviceDetails!!.size) {

                modelIdList.add(deviceDetails[i].split(",")[0])
                if (deviceDetails[i].split(",")[1] == bleTypeId) {
                    deviceIds = deviceDetails[i].split(",")[2]
                } else if (deviceDetails[i].split(",")[1] == dgpsTypeId) {
                    dgpsId = deviceDetails[i].split(",")[2]
                    Log.d("TAG", "fetchDetails: " + dgpsId)
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
            Log.d(
                TAG, "fetchDetails: finishedModelType--$finishedModelType \n" +
                        "modelName--$modelName \n" +
                        "profileName--$profileName \n" +
                        "deviceIds--$deviceIds \n" +
                        "dgpsId--$dgpsId \n" +
                        "dgpsIdRadio--$dgpsIdRadio \n" +
                        "deviceModule--${deviceModule.toString()}"
            )


            sharedPreferences!!.putStringData(Constants.MAKE, finishedModelType!!)
            sharedPreferences!!.putStringData(Constants.MODEL, modelName)
            sharedPreferences!!.putStringData(Constants.PROFILENAME, profileName)
            sharedPreferences!!.putStringData(Constants.DEVICE_ID, deviceIds)
            sharedPreferences!!.putStringData(Constants.DGPS_DEVICE_ID, dgpsId)
            sharedPreferences!!.putStringData(Constants.DGPS_DEVICE_ID_FOR_RADIO, dgpsIdRadio)
            sharedPreferences!!.putStringData(Constants.MODULE_DEVICE, deviceModule.toString())
            sharedPreferences!!.putStringData(Constants.DEVICE_NAME, firstFourChars)
        } catch (e: Exception) {
            Log.d(TAG, "fetchDetails: Exception --${e.message}")
        }


    }


    fun getcommandforparsing(opid: Int, oppid: Int) {
        if (opid > 0) {
            gnssdelay = dbControl.delaylist(opid, dgps_id)
            gnsscommands = dbControl.commandforparsinglist(opid, dgps_id)
            gnnsFormatCommands = dbControl.commandformatparsinglist(opid, dgps_id)
            Log.d(
                TAG,
                "getcommandforparsing: $gnsscommands \n ${gnsscommands.size} \n $opid "
            )
        } else if (oppid > 0) {
            radiodelay = dbControl.delaylist(oppid, dgps_id)
            radiocommands = dbControl.commandforparsinglist(oppid, dgps_id)
            radioFormatCommands = dbControl.commandformatparsinglist(oppid, dgps_id)
            Log.d(TAG, "NewListgetcommandforparsing: $oppid\n$radiocommands")

        }
    }

    fun setDeviceInfo() {
        val make = sharedPreferences!!.getStringData(Constants.MAKE)
        val model = sharedPreferences!!.getStringData(Constants.MODEL)
        val profileName = sharedPreferences!!.getStringData(Constants.PROFILENAME)

        binding.tvmake.text = make
        binding.tvmodel.text = model
        binding.tvprofileName.text = profileName
    }


    private fun send() {

        try {
            if (isRTKPPK) {
//                Utils.isFileWrite = true
//                if (Utils.isBTConnected) {
                try {
                    val str = "Raw On"
//                        val msgs = (str + newline).toByteArray()
//                        Utils.service!!.write(msgs)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
//                }

                val tstamp = System.currentTimeMillis()
//                Utils.fileName = "apogeePPK_$tstamp.apg" //like 2016_01_12.txt
//                preferenceStore.setFileName(Utils.fileName)
            }

//            dataconversion()

        } catch (e: java.lang.Exception) {
            Log.d(TAG, "send: ${e.message}")

        }
    }

    private fun getResponse() {
        Log.d(TAG, "getResponse: IN")
        try {
            lifecycleScope.launch {
                bleConnectionViewModel.bleResponse.collect {
                    if (it != null) {
                        when (it) {
                            is BleResponse.OnConnected -> {
                                BluetoothScanDeviceFragment.BTConnected = true
                            }


                            is BleResponse.OnConnectionClose -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: " + it.message)
                            }

                            is BleResponse.OnDisconnected -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: " + it.message)
                            }

                            is BleResponse.OnError -> {
                                BluetoothScanDeviceFragment.BTConnected = false
                                Log.d("ADD_GNSS_TEST", "getResponse: " + it.message)
                            }

                            is BleResponse.OnLoading -> {
                            }

                            is BleResponse.OnReconnect -> Log.d(
                                "ADD_GNSS_TEST",
                                "getResponse: " + it.message
                            )

                            is BleResponse.OnResponseRead -> {
                                Log.d("ADD_GNSS_TEST", "getResponse:GNSS ${it.response.data}")
                            }


                            is BleResponse.OnResponseWrite -> Log.d(
                                "ADD_GNSS_TEST",
                                "getResponse: " + it.isMessageSend
                            )

                        }
                    }
                }
            }

        } catch (e: Exception) {
            Log.d(TAG, "getResponse: Exception ${e.message}")
        }
    }

    override fun onPause() {
        super.onPause()
//bleConnectionViewModel.bleConnectionRepository.unBindService()
    }


    override fun onResume() {
        super.onResume()
        bleConnectionViewModel.bindService()
    }
//    fun dataconversion() {
//        delaylist.clear()
//        commandsformatList.clear()
//        commandsfromlist.clear()
//        newCommandList.clear()
//        if(pdaMapProfile.size>0)
//        {
//            val pda_off_opid = dbControl.getOperationId(getString(R.string.pda_off))
//            val dis_commandid = dbControl.commandidls1(pda_off_opid, dgps_id)
//            Log.d(TAG, "dataconversion:dis_commandid $dis_commandid")
//            val formatCommands = dbControl.commandformatparsinglist(pda_off_opid, dgps_id)
//            if (dis_commandid.size > 0) {
//                val pdaOffcommand = dbControl.getCommand(dis_commandid[0])
//                delaylist.add("100")
//                commandsfromlist.add(pdaOffcommand!!)
//                commandsformatList.add(formatCommands[0])
//            }
//        }
//        commandsfromlist.addAll(gnsscommands)
//        Log.d(TAG, "dataconversion: " + gnssdelay + "\n" + radiodelay + "\n" + gnsscommands + "\n" + radioFormatCommands+ "\n" + pdaMapProfile)
//        delaylist.addAll(gnssdelay)
//        commandsformatList.addAll(gnnsFormatCommands)
//        System.out.println(pdaMapProfile.size)
//        for (param in radiodelay) {
//            delaylist.add(param)
//        }
//
//        for (param in radioFormatCommands) {
//            commandsformatList.add(param)
//        }
//        Log.d(TAG, "dataconversionRadio: "+radiocommands)
//        for (param in radiocommands) {
//            Log.d(TAG, "dataconversionradiocommands: "+param)
//            val colums = param.split("(?i)2C".toRegex()).toTypedArray()
//            if (colums.size > 2) {
//                if (!devicedetail.isNullOrEmpty()) {
//                    val columss: Array<String> =
//                        devicedetail.split(",".toRegex()).toTypedArray()
//                    Log.d(TAG, "dataconversion: columss $columss \n ${columss.size}")
//                    var getvall = columss[2].trim { it <= ' ' }
//                    getvall = stringtohex(getvall)
//                    colums[2] = getvall
//                } else if (!Utils.devicedetail.isNullOrEmpty()) {
//                    val columss: Array<String> =
//                        Utils.devicedetail.split(",".toRegex()).toTypedArray()
//                    var getvall = columss[2].trim { it <= ' ' }
//                    getvall = stringtohex(getvall)
//                    colums[2] = getvall
//                }
//                colums[4] = "32"
//                if (isRTKPPK) {
//                    colums[28] = "32"
//                }
//                val sb = StringBuffer()
//                for (i in colums.indices) {
//                    sb.append(colums[i] + "2C")
//                }
//                val str = sb.toString()
//                commandsfromlist.add(str)
//
//            }else
//            {
//                commandsfromlist.add(param)
//            }
//
//        }
//
//
//        Log.d(TAG, "dataconversion: commandsfromlist---$commandsfromlist")
//        editCommand(commandsfromlist)
//    }


}