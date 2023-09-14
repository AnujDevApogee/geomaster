package com.apogee.geomaster.ui.configuration.deviceconfig

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.broadcast.WifiReceiver
import com.apogee.geomaster.databinding.FragmentNewConnectionSourceBinding
import com.apogee.geomaster.databinding.FragmentWiFiSetupBinding
import com.apogee.geomaster.model.MultiView.ItemType
import com.apogee.geomaster.model.MultiView.OnItemValueListener
import com.apogee.geomaster.model.MultiView.RecyclerlViewAdapter
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.Conversion
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.setUpDialogBox
import com.apogee.geomaster.utils.toastMsg
import com.apogee.socketlib.SocketBuilder
import com.apogee.socketlib.SocketClient
import com.apogee.socketlib.listner.ConnectionResponse
import com.apogee.socketlib.listner.SocketListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicBoolean


class WiFiSetupFragment : Fragment(R.layout.fragment_wi_fi_setup), OnItemValueListener {
    private lateinit var binding: FragmentWiFiSetupBinding

    val TAG = "WiFiSetupFragment"
    var gnssmodulename = ""
    var dgps_id = 0
    private val args by navArgs<WiFiSetupFragmentArgs>()
    private lateinit var dbTask: DatabaseRepsoitory

    var recycerlViewAdapter: RecyclerlViewAdapter? = null
    var map1: LinkedHashMap<String, String> = LinkedHashMap()
    var TypeList: ArrayList<ItemType> = ArrayList()
    var inputlists: ArrayList<Int>? = null
    var listDataSource: ArrayList<HashMap<String, String>> = ArrayList()
    var mapParameters: HashMap<String, String> = HashMap()
    var inputParam = ""
    var IP = ""
    var Port = ""
    private lateinit var socketClient: SocketClient

    var selectionV = HashMap<String, String>()
    lateinit var wifiManager: WifiManager
    private val MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1
    var receiverWifi: WifiReceiver? = null

    var isSSIDShow = false
    var updateUIReciver: BroadcastReceiver? = null
    var isFunCallFirt = true
    var isEdited: Boolean = false
    var data = "\nGET / HTTP/1.0\n" +
            "User-Agent: NTRIP ApogeeGnss\n" +
            "Accept: */*\n" +
            "Connection: close\r\n\r\n"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbTask = DatabaseRepsoitory(requireContext())
        binding = FragmentWiFiSetupBinding.bind(view)
        wifiManager =
            requireActivity().getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val sharedPreferences = MyPreference.getInstance(requireContext())
        val dgpsid: String? = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID)!!
        gnssmodulename=args.gnssmodulename
        if (dgpsid != null) {
            dgps_id = dgpsid.toInt()
        }

        val opppid = dbTask.getOperationId(getString(R.string.wifi))

        editpoint(opppid)



        binding.done.setOnClickListener {
            binding.done.requestFocus()
            /*REQUEST FOCUS */

            CorrectionFragment.wifiMap = map1

            Log.d(TAG, "onCreate: " + mapParameters + "==== map1 : " + map1.keys)
            if (mapParameters.keys == map1.keys) {
                if (binding.done.text == getText(R.string.done)) {
                    val id = dbTask.getidDataSource()
                    var dataSourceId = id.toInt()
                    dataSourceId++
                    for ((key, value) in map1) {
                        Log.d(TAG, "mapParameters: " + key.trim() + "======" + value.trim())
                        for (index in TypeList.indices) {

                        }
                        dbTask.insertdataSorcestable(
                            getString(R.string.wifi),
                            key.trim(),
                            value.trim(),
                            dataSourceId.toString().trim(),
                            "",
                            gnssmodulename.trim()
                        )
                    }
                    requireActivity().supportFragmentManager.popBackStack()

                } else if (binding.done.text == getText(R.string.get_mount_point)) {

                    binding.progressBarCyclic.visibility = View.VISIBLE
                    binding.done.isEnabled = false
                    binding.done.isClickable = false
                    var ssid = ""
                    var ssid_password = ""
                    if (map1.containsKey("IP")) {
                        IP = map1["IP"]!!.trim()
                    }

                    if (map1.containsKey("Port")) {
                        Port = map1["Port"]!!.trim()
                    }
                    if (map1.containsKey("SSID")) {
                        ssid = map1["SSID"]!!.trim();
                    }
                    if (map1.containsKey("SSID Password")) {
                        ssid_password = map1["SSID Password"]!!.trim();

                    }

                    if (ssid != null) {

                        if (!isNetworkConnectionAvailable) {
                            checkNetworkConnection()
                        } else {
                            if (IP.isNotEmpty() && Port.isNotEmpty()) {
                                IP = Conversion(requireContext()).hextToString(IP).toString()
                                Port = Conversion(requireContext()).hextToString(Port).toString()
                                getMountPoint(IP, Port)
                            }
                        }
                        /*finallyConnect(
                            Utils().hextToString(ssid_password).toString(),
                            Utils().hextToString(ssid).toString()
                        )*/
                        var result = dbTask.insertSSIDPassword(
                            Conversion(requireContext()).hextToString(ssid).toString(),
                            Conversion(requireContext()).hextToString(ssid_password).toString()
                        )
                        if (result) {
                            Log.d(TAG, "onCreate: " + "data inserted")
                        } else {
                            Log.d(TAG, "onCreate: " + "data already exist")
                        }
                    } else {
                        if (!isNetworkConnectionAvailable) {
                            checkNetworkConnection()
                        } else {
                            if (IP.isNotEmpty() && Port.isNotEmpty()) {
                                IP = Conversion(requireContext()).hextToString(IP).toString()
                                Port = Conversion(requireContext()).hextToString(Port).toString()
                                getMountPoint(IP, Port)
                            }
                        }
                    }
                    Log.d(
                        TAG,
                        "onCreate: " + Conversion(requireContext()).hextToString(ssid)
                            .toString() + "=====" + Conversion(requireContext()).hextToString(
                            ssid_password
                        ).toString()
                    )
                }

            } else {
                requireActivity().toastMsg(getString(R.string.please_fill_all_data))
            }
        }


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                MY_PERMISSIONS_ACCESS_COARSE_LOCATION
            )
        } else {
            wifiManager.startScan()
        }
//        progressDialog = Utils().progressDialog(this, "Conecting...")
        /*
          progressDialog.show()
          */
        val filter = IntentFilter()
        filter.addAction("service.to.activity.transfer")

        binding.refreshSSID.setOnClickListener {
            isSSIDShow = false
            isFunCallFirt = false
//            progressDialog.show()
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            requireContext().registerReceiver(receiverWifi, intentFilter)
            requireContext().registerReceiver(updateUIReciver, filter)
            getWifi()
        }

        updateUIReciver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //UI update here

                if (intent != null) {

                    try {
                        val ssidlist =
                            intent.getSerializableExtra("deviceList") as ArrayList<String>
                        setSSIDList(ssidlist)
                    } catch (ex: Exception) {

                    }

                }
            }
        }
        //   registerReceiver(updateUIReciver, filter)

        //  val config = getWifiApConfiguration()

        if (listDataSource.size > 0) {
        } else {

            if (gnssmodulename == getString(R.string.rover)) {

                // TypeList.removeA
                // t(5)

                var userNamePosition: Int = -1
                Log.d(TAG, "onCreate: " + TypeList.toString())

                if (TypeList.size > 0) {
                    for (i in TypeList.indices) {
                        if (TypeList.get(i).getTitle().equals("Username") != null) {
                            if (TypeList.get(i).getTitle().equals("Username")) {
                                userNamePosition = i;
                            }
                        }

                        if (TypeList.get(i).getTitle().equals("NTRIP Password") != null) {
                            if (TypeList.get(i).getTitle().equals("NTRIP Password")) {
                                TypeList.removeAt(i)
                                TypeList.add(
                                    userNamePosition + 1,
                                    ItemType(
                                        ItemType.INPUTTYPE,
                                        "NTRIP Password",
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        false,
                                        false
                                    )
                                )
                            }
                        }
                    }
                    TypeList.add(
                        5,
                        ItemType(
                            ItemType.INPUTTYPE,
                            "SSID",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            false,
                            false
                        )
                    )
                }
                mapParameters["SSID"] = ""
                Log.d(TAG, "onCreate: " + TypeList)
                recycerlViewAdapter?.notifyDataSetChanged()
            } else {
                //  TypeList.removeAt(4)
                if (TypeList.size > 0) {
                    TypeList.add(
                        5,
                        ItemType(
                            ItemType.INPUTTYPE,
                            "SSID",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            false,
                            false
                        )
                    )
                    mapParameters["SSID"] = ""
                    recycerlViewAdapter?.notifyDataSetChanged()
                }
            }
        }

        // on below line we are adding click listener for our toggle button
        // on below line we are adding click listener for our toggle button
        binding.idBtnToggle.setOnClickListener {
            // on below line we are checking if
            if (binding.idBtnToggle.isChecked) {
                // on below line we are setting message
                // for status text view if toggle button is checked.
                if (!wifiManager.isWifiEnabled) {
                    requireActivity().toastMsg("Turning WiFi ON...")
                    wifiManager.isWifiEnabled = true
                }
                isSSIDShow = false
                isFunCallFirt = false
//                progressDialog.show()
                val intentFilter = IntentFilter()
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                requireContext().registerReceiver(receiverWifi, intentFilter)
                requireContext().registerReceiver(updateUIReciver, filter)
                getWifi()
                binding.refreshSSID.isEnabled = true
                binding.refreshSSID.isClickable = true
                binding.refreshSSID.isFocusable = true

            } else {
                Log.d(TAG, "onCreate: " + TypeList)
                // on below line we are setting message for
                // status text view if toggle button is un checked.
                var index = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID")) {
                        index = i
                        break
                    }
                }
                if (gnssmodulename == getString(R.string.rover)) {

                    TypeList.removeAt(index)
                    TypeList.add(
                        index,
                        ItemType(
                            ItemType.INPUTTYPE,
                            "SSID",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            false,
                            false
                        )
                    )
                    mapParameters["SSID"] = ""
                    recycerlViewAdapter?.notifyDataSetChanged()
                } else {

                    TypeList.removeAt(index)
                    TypeList.add(
                        index,
                        ItemType(
                            ItemType.INPUTTYPE,
                            "SSID",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            false,
                            false
                        )
                    )
                    mapParameters["SSID"] = ""
                    recycerlViewAdapter?.notifyDataSetChanged()
                }
                binding.refreshSSID.isEnabled = false
                binding.refreshSSID.isClickable = false
                binding.refreshSSID.isFocusable = false
            }
        }


    }

    fun editpoint(opid: Int) {
        var selectionV: Map<String, String>
        recycerlViewAdapter = RecyclerlViewAdapter(TypeList, this)
        binding.recyclerView.adapter = recycerlViewAdapter
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = mLayoutManager
        val commandls: List<Int?>
        commandls = dbTask.commandidls1(opid, dgps_id)
        val joined = TextUtils.join(", ", commandls)
        val selectionidlist: ArrayList<Int?>
        selectionidlist = dbTask.selectionidlist1(joined) as ArrayList<Int?>
        val joined2 = TextUtils.join(", ", selectionidlist)

        /*View for selection recyclerview*/
        val inputlist: ArrayList<Int?>
        inputlist = dbTask.inputlist(joined) as ArrayList<Int?>
        inputlists = dbTask.inputlist(joined) as ArrayList<Int>
        val joined3 = TextUtils.join(", ", inputlist)
        val selectionList: Map<String, Map<String, String>>
        selectionList = dbTask.displayvaluelist1(joined2)
        val selectionParameter = selectionList.keys
        for (param in selectionParameter) {
            selectionV = selectionList[param]!!
            val baudratekey = selectionV.keys
            val baudratevalue = selectionV.values
            val baudratevaluelist: ArrayList<String> = ArrayList(baudratekey)
            baudratevaluelist.add(0, "--select--")
            TypeList.add(ItemType(ItemType.DROPDOWNTYPE, param, selectionV))
            mapParameters[param] = ""
            recycerlViewAdapter!!.notifyDataSetChanged()
            println("Initial values : $baudratevalue") //
        }
        listDataSource.clear()
        var count = 1
        val id = dbTask.getidDataSource()
        for (i in 0 until id.toInt()) {
            if (count <= id.toInt()) {
                val dataSourceList =
                    dbTask.getDataSource(getString(R.string.wifi), count.toString(), gnssmodulename)
                Log.d("TAG", "showWifiData: " + dataSourceList)
                if (dataSourceList!!.isNotEmpty()) {
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }


        if (listDataSource.size > 0) {
            if (gnssmodulename == getString(R.string.rover)) {
                for ((key, value) in listDataSource!![listDataSource.size - 1].entries) {
                    Log.d(TAG, "listDataSource: " + key + "====" + value)
                    try {
                        if (key != "param_id" && key != "operation") {
                            val output = StringBuilder()
                            var i = 0
                            while (i < value.length) {
                                val str: String = value.substring(i, i + 2)
                                output.append(str.toInt(16).toChar())
                                i += 2
                            }
                            if (key != "Mount-Point") {
                                if (key != "Toggle Previous Configuration") {
                                    if (key == "Username") {
                                        TypeList.add(
                                            1,
                                            ItemType(
                                                ItemType.INPUTTYPE,
                                                key,
                                                null,
                                                null,
                                                null,
                                                null,
                                                output.toString(),
                                                null,
                                                false,
                                                false
                                            )
                                        )
                                        mapParameters[key] = output.toString()
                                        map1[key] = value.toString()
                                        recycerlViewAdapter!!.notifyDataSetChanged()
                                    } else if (key == "NTRIP Password") {
                                        TypeList.add(
                                            ItemType(
                                                ItemType.INPUTTYPE,
                                                key,
                                                null,
                                                null,
                                                null,
                                                null,
                                                output.toString(),
                                                null,
                                                false,
                                                false
                                            )
                                        )
                                        mapParameters[key] = output.toString()
                                        map1[key] = value!!.trim().toString()
                                        recycerlViewAdapter!!.notifyDataSetChanged()
                                    } else if (key == "SSID") {
                                        TypeList.add(
                                            ItemType(
                                                ItemType.INPUTTYPE,
                                                key,
                                                null,
                                                null,
                                                null,
                                                null,
                                                output.toString(),
                                                null,
                                                false,
                                                false
                                            )
                                        )
                                        mapParameters[key] = output.toString()
                                        map1[key] = value!!.trim().toString()
                                        recycerlViewAdapter!!.notifyDataSetChanged()
                                    } else {
                                        if (key != "SSID") {
                                            TypeList.add(
                                                ItemType(
                                                    ItemType.INPUTTYPE,
                                                    key,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    output.toString(),
                                                    null,
                                                    false,
                                                    false
                                                )
                                            )
                                            mapParameters[key] = output.toString()
                                            map1[key] = value!!.trim().toString()
                                            recycerlViewAdapter!!.notifyDataSetChanged()
                                        }
                                    }
                                }
                            } else {
                                Log.d("TAG", "editpoint: " + key + "==" + output)
                                inputParam = key
                                binding.done.text = getText(R.string.get_mount_point)
                            }

                        }
                    } catch (ex: Exception) {

                    }


                }
            } else {
                for ((key, value) in listDataSource!![listDataSource.size - 1].entries) {

                    if (key != "param_id" && key != "operation") {
                        val output = StringBuilder()
                        var i = 0
                        while (i < value.length) {
                            val str: String = value.substring(i, i + 2)
                            output.append(str.toInt(16).toChar())
                            i += 2
                        }
                        if (key != "Toggle Previous Configuration") {
                            if (key != "Username") {
                                TypeList.add(
                                    ItemType(
                                        ItemType.INPUTTYPE,
                                        key,
                                        null,
                                        null,
                                        null,
                                        null,
                                        output.toString(),
                                        null,
                                        false,
                                        false
                                    )
                                )
                                mapParameters[key] = output.trim().toString()
                                map1[key] = value!!.trim().toString()
                                recycerlViewAdapter!!.notifyDataSetChanged()

                            }
                        }

                    }
                }

            }
        } else {
            /*View for Input recyclerview*/
            if (gnssmodulename == getString(R.string.rover)) {
                val inputparameterlist: ArrayList<String>
                inputparameterlist = dbTask.inputparameterlist(joined3) as ArrayList<String>
                for (inputparam in inputparameterlist) {
                    val name = inputparam.split(",".toRegex()).toTypedArray()[0]
                    val remark = inputparam.split(",".toRegex()).toTypedArray()[2]
                    Log.d(TAG, "editpoint: " + name + "=====" + remark)
                    // check remark if MountPoint then show button fetch Mount point from server
                    if (remark != "MountPoint") {
                        if (name == "Username") {
                            TypeList.add(
                                1,
                                ItemType(
                                    ItemType.INPUTTYPE,
                                    inputparam.split(",".toRegex()).toTypedArray()[0],
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    false,
                                    false
                                )
                            )
                            mapParameters[inputparam.split(",".toRegex()).toTypedArray()[0]] = ""
                            recycerlViewAdapter!!.notifyDataSetChanged()
                        } else {
                            if (name != "SSID") {
                                TypeList.add(
                                    ItemType(
                                        ItemType.INPUTTYPE,
                                        inputparam.split(",".toRegex()).toTypedArray()[0],
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        false,
                                        false
                                    )
                                )
                                mapParameters[inputparam.split(",".toRegex()).toTypedArray()[0]] =
                                    ""
                                recycerlViewAdapter!!.notifyDataSetChanged()
                            }
                        }
                    } else {
                        inputParam = name
                        binding.done.text = getText(R.string.get_mount_point)
                    }
                }
            } else {
                val inputparameterlist: ArrayList<String>
                inputparameterlist = dbTask.inputparameterlists(joined3) as ArrayList<String>
                for (inputparam in inputparameterlist) {
                    if (inputparam != "Username" && inputparam != "SSID") {
                        TypeList.add(
                            ItemType(
                                ItemType.INPUTTYPE,
                                inputparam,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                false,
                                false
                            )
                        )
                        mapParameters[inputparam] = ""
                        recycerlViewAdapter!!.notifyDataSetChanged()

                    }


                }
            }
            Log.d(TAG, "TypeList: " + TypeList)
        }

    }


    fun getMountPoint(ip: String, port: String) {
        try {
            socketClient = SocketBuilder()
                .newBuilder(lifecycleScope.coroutineContext)
                .addCallBack(listener = callback)
                .addIpAddress(ip)
                .addPort(port.toInt())
                .build()

            socketClient.establishConnection(requireContext())

        } catch (e: IOException) {
            e.printStackTrace()
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    requireActivity().setUpDialogBox(
                        "Alert!",
                        "Socket Connection Failed \n" +
                                " Please Check IP & Port.",
                        "Continue",
                        "Cancel",
                        success = {
                        },
                        cancelListener = {

                        })

                } catch (e: WindowManager.BadTokenException) {
                    //use a log message
                }

            }
        }
        // binding.progressBarCyclic.visibility = View.GONE
    }


    override fun returnValue(title: String?, finalvalue: String?) {
        if (!finalvalue.equals("--select--", ignoreCase = true)) {
            val titl = title!!
            if (title != "Name") {
                var index = -1
                var indexPassward = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID")) {
                        index = i
                    }
                    if (TypeList.get(i).getTitle().equals("SSID Password")) {
                        indexPassward = i

                    }
                }
                var type = TypeList.get(index)
                if (title == "SSID" && !map1.containsValue(finalvalue!!.trim()) && type.type1 == ItemType.DROPDOWNTYPE) {
                    val ssid = Conversion(requireContext()).hextToString(finalvalue)
                    Log.d(TAG, "returnValue: " + ssid)
                    val password = dbTask.getSsidPassword(ssid.toString())
                    Log.d(TAG, "returnValue: " + title + "===" + password)
                    if (!TextUtils.isEmpty(password)) {
                        if (TypeList.size == 7) {
                            TypeList.removeAt(indexPassward)
                            TypeList.add(
                                indexPassward,
                                ItemType(
                                    ItemType.INPUTTYPE,
                                    "SSID Password",
                                    null,
                                    null,
                                    null,
                                    null,
                                    password,
                                    null,
                                    false,
                                    false
                                )
                            )
                            mapParameters["SSID Password"] = password
                            map1["SSID Password"] =
                                Conversion(requireContext()).stringtohex(password)
                            recycerlViewAdapter!!.notifyItemChanged(indexPassward)
                        }
                    }
                }
                Log.d(TAG, "finalvalue: " + finalvalue)
                map1[titl.trim()] = finalvalue!!.trim()
            }
        }
    }

    val isNetworkConnectionAvailable: Boolean
        @SuppressLint("MissingPermission")
        get() {
            val cm =
                requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnected

            return isConnected
        }

    /*CheckNetworkConnection*/
    private fun checkNetworkConnection() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setIcon(R.drawable.noconnection)
        builder.setTitle(getText(R.string.no_internet_connection))
        builder.setMessage(getText(R.string.please_turn_on_internet_connection_to_continue))
        builder.setNegativeButton(getText(R.string.ok)) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun returnValue(
        title: String?,
        finalvalue: String?,
        position: Int,
        operation: String?,
        elevation: String?
    ) {
    }

    override fun onResume() {
        super.onResume()
        if (!isSSIDShow) {
            receiverWifi = WifiReceiver(wifiManager)
            val intentFilter = IntentFilter()
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            requireContext().registerReceiver(receiverWifi, intentFilter)
            getWifi()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_ACCESS_COARSE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requireActivity().toastMsg("permission granted")
                wifiManager.startScan()
            } else {
                requireActivity().toastMsg("permission not granted")
                return
            }
        }
    }


    private fun getWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION
                )
            } else {
                wifiManager.startScan()
            }
        } else {
            wifiManager.startScan()
        }
    }

    fun setSSIDList(list: ArrayList<String>) {
        if (list.isNotEmpty() && !isSSIDShow) {
            requireActivity().unregisterReceiver(receiverWifi)
            requireActivity().unregisterReceiver(updateUIReciver)
            val selectionV = HashMap<String, String>()
            for (i in 0 until list.size) {
                selectionV[list[i]] = Conversion(requireContext()).hexString(list[i])
            }

            if (gnssmodulename == getString(R.string.rover)) {
                var index = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID")) {
                        index = i
                        break
                    }
                }
                if (!isFunCallFirt) {
                    TypeList.removeAt(index)
                }
                TypeList.add(index, ItemType(ItemType.DROPDOWNTYPE, "SSID", selectionV))
                mapParameters["SSID"] = ""
                recycerlViewAdapter?.notifyDataSetChanged()
            } else {
                Log.d(TAG, "setSSIDList: " + TypeList)
                var index = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID")) {
                        index = i
                        break
                    }
                }

                if (!isFunCallFirt) {
                    TypeList.removeAt(index)
                }
                TypeList.add(index, ItemType(ItemType.DROPDOWNTYPE, "SSID", selectionV))
                mapParameters["SSID"] = ""
                recycerlViewAdapter?.notifyDataSetChanged()

            }

            isSSIDShow = true
        } else if (!isSSIDShow) {
            if (gnssmodulename == getString(R.string.rover)) {
                var index = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID", true)) {
                        index = i
                        break
                    }
                }
                if (!isFunCallFirt) {
                    TypeList.removeAt(index)
                }
                TypeList.add(
                    index,
                    ItemType(
                        ItemType.INPUTTYPE,
                        "SSID",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false,
                        false
                    )
                )
                mapParameters["SSID"] = ""
                recycerlViewAdapter?.notifyDataSetChanged()
            } else {
                var index = -1
                for (i in TypeList.indices) {
                    if (TypeList.get(i).getTitle().equals("SSID")) {
                        index = i
                        break
                    }
                }
                Log.d(TAG, "setSSIDListBase: " + TypeList)
                if (!isFunCallFirt) {
                    TypeList.removeAt(index)
                }
                TypeList.add(
                    index,
                    ItemType(
                        ItemType.INPUTTYPE,
                        "SSID",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false,
                        false
                    )
                )
                mapParameters["SSID"] = ""
                recycerlViewAdapter?.notifyDataSetChanged()
            }
        }
    }


    val callback = object : SocketListener {
        override fun socketListener(conn: ConnectionResponse) {
            when (conn) {
                is ConnectionResponse.OnConnected -> {
                    socketClient.onRequestSent(data)
                }

                is ConnectionResponse.OnDisconnect -> {

                    GlobalScope.launch(Dispatchers.Main) {
                        if (selectionV.size > 0) {
                            TypeList.add(
                                ItemType(
                                    ItemType.DROPDOWNTYPE,
                                    inputParam,
                                    selectionV
                                )
                            )
                            mapParameters[inputParam] = ""
                            recycerlViewAdapter?.notifyItemInserted(7)
                            binding.done.text = getText(R.string.done)
                            binding.progressBarCyclic.visibility = View.GONE
                            binding.done.isEnabled = true
                            binding.done.isClickable = true

                        } else {
                            TypeList.add(
                                ItemType(
                                    ItemType.INPUTONLYTEXT,
                                    inputParam,
                                    null,
                                    "not available",
                                    null,
                                    null,
                                    null,
                                    null,
                                    false,
                                    false
                                )
                            )
                            mapParameters[inputParam] = ""
                            recycerlViewAdapter?.notifyItemChanged(7)
                            binding.done.text = getText(R.string.done)
                            binding.progressBarCyclic.visibility = View.GONE
                            binding.done.isEnabled = true
                            binding.done.isClickable = true

                        }
                    }
                }

                is ConnectionResponse.OnNetworkConnection -> {
                }

                is ConnectionResponse.OnRequestError -> {

                }

                is ConnectionResponse.OnResponse -> {

                    try {
                        selectionV.clear()
                        val avai = conn.response.size
                        Log.d("test=", "test")
                        if (avai > 0) {
                            Log.d("test==", "test")

                            val str = String(conn.response, StandardCharsets.UTF_8)
                            val mountData = str.split("STR".toRegex())
                            for (i in 1 until mountData.size) {
                                val mountPoint = mountData[i].split(";".toRegex()).toTypedArray()[1]
                                Log.d("test===", mountPoint)
                                selectionV[mountPoint] =
                                    Conversion(requireContext()).convertStringToHex(mountPoint)
                            }
                            if (selectionV.size > 0) {
                                socketClient.disconnect()
                            }


                        } else {
                            GlobalScope.launch(Dispatchers.Main) {
                                TypeList.add(
                                    ItemType(
                                        ItemType.INPUTTYPE,
                                        inputParam,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        false,
                                        false
                                    )
                                )
                                mapParameters[inputParam] = ""
                                recycerlViewAdapter?.notifyItemInserted(7)
                                binding.done.text = getText(R.string.done)
                                binding.progressBarCyclic.visibility = View.GONE
                                binding.done.isEnabled = true
                                binding.done.isClickable = true

                            }


                        }
                    } catch (e: IOException) {
                        e.printStackTrace()

                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                is ConnectionResponse.OnResponseError -> {
                }

            }
        }
    }


}