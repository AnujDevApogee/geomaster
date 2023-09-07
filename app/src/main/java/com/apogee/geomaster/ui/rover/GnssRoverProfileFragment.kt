package com.apogee.geomaster.ui.rover

import android.content.ContentValues
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentGnssRoverProfileBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.updatedblelibrary.Utils.BleResponse
import com.apogee.updatedblelibrary.Utils.BleResponseListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean


class GnssRoverProfileFragment : Fragment(), TextToSpeech.OnInitListener, BleResponseListener {
    val TAG = "GnssRoverProfileFragment"
    val device_id = 56
    val dgps_id = 6
    var isRTKPPK = false
    var modeWork = ""
    var mode = ""
    var opid = 0
    private lateinit var dbControl: DatabaseRepsoitory
    var deviceInfotimerHandler = Handler(Looper.getMainLooper())
    private var mDeviceAddress: String? = null
    var services = false
    var mPlayer: MediaPlayer? = null
    var ispubx = false
    var deviceinfo = false
//    var mBluetoothLeService: BluetoothLeService? = null
    var deviceName = ""
    lateinit var sharedPreferences: SharedPreferences
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
        binding = FragmentGnssRoverProfileBinding.bind(view)
        dbControl = DatabaseRepsoitory(requireContext())



        if (isRTKPPK) {


            binding.triggerPoint.visibility = View.VISIBLE
            opid = dbControl.getOperationId(getString(R.string.rtk_ppk))
            deviceInfotimerHandler.postDelayed(deviceInfoRunnable, 1000)
        } else {
            opid = dbControl.getOperationId(getString(R.string.rover))
        }
        getcommandforparsing(opid, device_id)



        binding.done.setOnClickListener {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gnss_rover_profile, container, false)
    }

    override fun onInit(p0: Int) {
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

    var deviceInfoCounter = 0

    var deviceInfoRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isRTKPPK) {
                deviceInfoCounter++

                if (modeWork == "PPK" || modeWork == "$mode + PPK") {
//                         progressDialog.dismiss()
                    binding.done.isEnabled = false
                    binding.done.isClickable = false
                    binding.done.isFocusable = false
                    binding.done.setBackgroundResource(R.drawable.buttondesign1)
                    deviceInfotimerHandler.removeCallbacks(this)
                } else if (modeWork != "PPK" || modeWork != "$mode + PPK") {
//                         progressDialog.dismiss()
                    binding.triggerPoint.isEnabled = false
                    binding.triggerPoint.isClickable = false
                    binding.triggerPoint.isFocusable = false
                    binding.triggerPoint.setBackgroundResource(R.drawable.buttondesign1)
                    deviceInfotimerHandler.removeCallbacks(this)
                }

                if (deviceInfoCounter < 6) {
                    deviceInfotimerHandler.postDelayed(this, 1000)
                } else {

                    /*    if (progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }*/
                }
            }


        }
    }


/*
    fun main() {
        // Using a coroutine to perform an asynchronous task
        GlobalScope.launch {
            // Simulate some work for 1 second
            delay(1000)
            println("Coroutine completed")
        }

        // Keep the program running for a while to allow the coroutine to complete
        Thread.sleep(2000)
    }
*/




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





}