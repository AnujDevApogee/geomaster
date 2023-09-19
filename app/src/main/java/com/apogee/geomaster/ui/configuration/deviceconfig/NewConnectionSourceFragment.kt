package com.apogee.geomaster.ui.configuration.deviceconfig

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentNewConnectionSourceBinding
import com.apogee.geomaster.model.MultiView.ItemType
import com.apogee.geomaster.model.MultiView.OnItemValueListener
import com.apogee.geomaster.model.MultiView.RecyclerlViewAdapter
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.Conversion
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.createLog
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
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.atomic.AtomicBoolean


class NewConnectionSourceFragment : Fragment(R.layout.fragment_new_connection_source),
    OnItemValueListener {
    private lateinit var binding: FragmentNewConnectionSourceBinding
    private val args by navArgs<NewConnectionSourceFragmentArgs>()
    private lateinit var dbControl: DatabaseRepsoitory
    var TypeList: ArrayList<ItemType> = ArrayList()
    var recycerlViewAdapter: RecyclerlViewAdapter? = null


    var dgps_id = 0
    var radiocommands: ArrayList<String> = ArrayList()
    var radiodelay: ArrayList<String> = ArrayList()
    var commandsfromlist: ArrayList<String> = ArrayList()
    var delaylist: ArrayList<String> = ArrayList()
    var gnssdelay: ArrayList<String> = ArrayList()
    var gnsscommands: ArrayList<String> = ArrayList()
    var gnnsFormatCommands: ArrayList<String> = ArrayList()
    var radioFormatCommands: ArrayList<String> = ArrayList()
    var selectionV = HashMap<String, String>()
    var gnssmodulename = ""
    var mapParameters: HashMap<String, String> = HashMap()
    var inputlists: ArrayList<Int>? = null
    var map1: LinkedHashMap<String, String> = LinkedHashMap()
    var listDataSource: ArrayList<HashMap<String, String>> = ArrayList()
    private val working = AtomicBoolean(true)

    //    private var socket: Socket? = null
    private lateinit var socketClient: SocketClient
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null
    val TAG = "NewConnectionSourceFragment"
    var IP = ""
    var Port = ""
    var inputParam = ""
    private val data = "\nGET / HTTP/1.0\n" +
            "User-Agent: NTRIP ApogeeGnss\n" +
            "Accept: */*\n" +
            "Connection: close\r\n\r\n"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewConnectionSourceBinding.bind(view)
        dbControl = DatabaseRepsoitory(requireContext())


        gnssmodulename = args.gnssmodulename
        val sharedPreferences = MyPreference.getInstance(requireContext())
        val dgpsid: String? = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID)!!
        if (dgpsid != null) {
            dgps_id = dgpsid.toInt()
        }

        val opppid = dbControl.getOperationId(getString(R.string.gsm))
        radiocommands.clear()
        radiodelay.clear()
        commandsfromlist.clear()
        delaylist.clear()
        getcommandforparsing(0, opppid)
        editpoint(opppid)

        binding!!.refreshSSID.visibility = View.GONE
        binding!!.idBtnToggle.visibility = View.GONE

        binding!!.done.setOnClickListener {


/*
            binding!!.done.requestFocus()
            */
            /* REQUEST FOCUS */




            for (key in mapParameters.keys) {
                Log.d("paramName_key", key)
//                Log.d("paramNamekey_value", key.value)
            }

            for (values in map1.values) {
                Log.d("paramName==", values)
            }
            CorrectionFragment.roverMap = map1
            if (mapParameters.keys == map1.keys) {
                if (binding!!.done.text == getText(R.string.done)) {
                    val id = dbControl.getidDataSource()
                    var dataSourceId = id.toInt()
                    dataSourceId++
                    for ((key, value) in map1) {
                        dbControl.insertdataSorcestable(
                            getString(R.string.gsm),
                            key,
                            value,
                            dataSourceId.toString(),
                            "",
                            gnssmodulename
                        )
                    }
                    requireActivity().supportFragmentManager.popBackStack()

                } else if (binding.done.text == getText(R.string.get_mount_point)) {
                    if (!isNetworkConnectionAvailable) {
                        checkNetworkConnection()
                    } else {
                        binding.progressBarCyclic.visibility = View.VISIBLE

                        binding.done.isEnabled = false
                        binding.done.isClickable = false
                        if (map1.containsKey("IP")) {
                            IP = map1["IP"]!!
                        }

                        if (map1.containsKey("Port")) {
                            Port = map1["Port"]!!
                        }

                        if (IP.isNotEmpty() && Port.isNotEmpty()) {
                            IP = Conversion(requireContext()).hextToString(IP).toString()
                            Port = Conversion(requireContext()).hextToString(Port).toString()

                            try {
                                socketClient = SocketBuilder()
                                    .newBuilder(lifecycleScope.coroutineContext)
                                    .addCallBack(listener = callback)
                                    .addIpAddress(IP)
                                    .addPort(Port.toInt())
                                    .build()


                                socketClient.establishConnection(requireContext())

                            } catch (e: IOException) {
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
                                e.printStackTrace()
                            }

                        }

                    }
                }

            } else {
                requireActivity().toastMsg(getString(R.string.please_fill_all_data))
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

    fun getcommandforparsing(opid: Int, oppid: Int) {
        if (opid > 0) {
            gnssdelay = dbControl.delaylist(opid, dgps_id)
            gnsscommands = dbControl.commandforparsinglist(opid, dgps_id)
            gnnsFormatCommands = dbControl.commandformatparsinglist(opid, dgps_id)
        } else if (oppid > 0) {
            radiodelay = dbControl.delaylist(oppid, dgps_id)
            radiocommands = dbControl.commandforparsinglist(oppid, dgps_id)
            radioFormatCommands = dbControl.commandformatparsinglist(oppid, dgps_id)
        }
    }


    fun editpoint(opid: Int) {
        var selectionV: Map<String, String>
        recycerlViewAdapter = RecyclerlViewAdapter(TypeList, this)
        binding!!.recyclerView.adapter = recycerlViewAdapter
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.layoutManager = mLayoutManager
        val commandls: List<Int?>
        commandls = dbControl.commandidls1(opid, dgps_id)
        val joined = TextUtils.join(", ", commandls)
        val selectionidlist: ArrayList<Int?>
        selectionidlist = dbControl.selectionidlist1(joined) as ArrayList<Int?>
        val joined2 = TextUtils.join(", ", selectionidlist)

        /*View for selection recyclerview*/
        val inputlist: ArrayList<Int?>
        inputlist = dbControl.inputlist(joined) as ArrayList<Int?>
        inputlists = dbControl.inputlist(joined) as ArrayList<Int>
        val joined3 = TextUtils.join(", ", inputlist)
        val selectionList: Map<String, Map<String, String>>
        selectionList = dbControl.displayvaluelist1(joined2)
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
        val id = dbControl.getidDataSource()
        for (i in 0 until id.toInt()) {
            if (count <= id.toInt()) {
                val dataSourceList =
                    dbControl.getDataSource(
                        getString(R.string.gsm),
                        count.toString(),
                        gnssmodulename
                    )
                Log.d("TAG", "showGSMData: " + dataSourceList)
                if (dataSourceList!!.isNotEmpty()) {
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }
        /*View for Input recyclerview*/
        if (listDataSource.size > 0) {
            if (gnssmodulename == getString(R.string.rover)) {
                for ((key, value) in listDataSource!![listDataSource.size - 1].entries) {

                    try {
                        if (key != "param_id" && key != "operation") {

                            val output = StringBuilder()
                            var i = 0
                            while (i < value.length) {
                                val str: String = value.substring(i, i + 2)
                                output.append(str.toInt(16).toChar())
                                i += 2
                            }
                            // check remark if MountPoint then show button fetch Mount point from server
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
                                    } else {
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
                                        map1[key] = value.toString()
                                        recycerlViewAdapter!!.notifyDataSetChanged()
                                    }
                                }
                            } else {
                                inputParam = key
                                binding!!.done.text = getText(R.string.get_mount_point)
                            }

                        }
                    } catch (e: Exception) {

                    }
                }
            } else {
                for ((key, value) in listDataSource!![listDataSource.size - 1].entries) {

                    try {
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
                                    mapParameters[key] = output.toString()
                                    map1[key] = value
                                    recycerlViewAdapter!!.notifyDataSetChanged()

                                }
                            }

                        }
                    } catch (e: Exception) {
                    }

                }
            }

        } else {

            if (gnssmodulename == getString(R.string.rover)) {
                val inputparameterlist: ArrayList<String>
                inputparameterlist = dbControl.inputparameterlist(joined3) as ArrayList<String>
                for (inputparam in inputparameterlist) {
                    val name = inputparam.split(",".toRegex()).toTypedArray()[0]
                    val remark = inputparam.split(",".toRegex()).toTypedArray()[2]
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
                            mapParameters[inputparam.split(",".toRegex()).toTypedArray()[0]] = ""
                            recycerlViewAdapter!!.notifyDataSetChanged()
                        }
                    } else {
                        inputParam = name
                        binding!!.done.text = getText(R.string.get_mount_point)
                    }
                }
            } else {
                val inputparameterlist: ArrayList<String>
                inputparameterlist = dbControl.inputparameterlists(joined3) as ArrayList<String>
                for (inputparam in inputparameterlist) {

                    if (inputparam != "Username") {
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

                    }/*else{
                    TypeList.add(ItemType(ItemType.INPUTTYPE, inputparam, null, null, null, null, null,false,false))
                    mapParameters[inputparam] = ""
                    recycerlViewAdapter!!.notifyDataSetChanged()

                }*/


                }
            }
        }
    }

    override fun returnValue(title: String?, finalvalue: String?) {
        if (!finalvalue.equals("--select--", ignoreCase = true)) {
            val titl = title!!
            val returnname = finalvalue!!

            map1[titl] = returnname
            Log.d(TAG, "returnValue:map1 $map1")


        }
    }

    val callback = object : SocketListener {
        override fun socketListener(conn: ConnectionResponse) {
            when (conn) {
                is ConnectionResponse.OnConnected -> {
                    Log.d(TAG, "socketListener:OnConnected ${conn.response}")
                    try{
                        socketClient.onRequestSent(data)

                    }catch (e:Exception){
                        Log.d(TAG, "socketListener: ${e.message}")
                    }

                }

                is ConnectionResponse.OnDisconnect -> {
                    Log.d(TAG, "socketListener:OnDisconnect ${conn.code} ")

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
                            recycerlViewAdapter?.notifyItemInserted(5)
                            binding!!.done.text = getText(R.string.done)
                            binding!!.progressBarCyclic.visibility = View.GONE
                            binding!!.done.isEnabled = true
                            binding!!.done.isClickable = true

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
                            recycerlViewAdapter?.notifyItemChanged(5)
                            binding!!.done.text = getText(R.string.done)
                            binding!!.progressBarCyclic.visibility = View.GONE
                            binding!!.done.isEnabled = true
                            binding!!.done.isClickable = true

                        }

                    }


                }

                is ConnectionResponse.OnNetworkConnection -> {
                    Log.d(TAG, "socketListener:OnNetworkConnection $conn")


                }

                is ConnectionResponse.OnRequestError -> {
                    Log.d(TAG, "socketListener:OnRequestError $conn")

                }

                is ConnectionResponse.OnResponse -> {
                    Log.d(TAG, "socketListener:OnResponse ${String(conn.response)}")
                    try {
                            selectionV.clear()
                            val avai = conn.response.size
                            if (avai > 0) {
                                val str = String(conn.response, StandardCharsets.UTF_8)
                                val mountData = str.split("STR".toRegex())
                                for (i in 1 until mountData.size) {
                                    val mountPoint =
                                        mountData[i].split(";".toRegex()).toTypedArray()[1]
                                    Log.d("test===", mountPoint)
                                    selectionV[mountPoint] =
                                        Conversion(requireContext()).convertStringToHex(mountPoint)
                                }
                                createLog("TAG_SOCKET", "sIZE OF Y IS ${selectionV.size}")
                                if (selectionV.size > 0) {
                                    createLog("TAG_SOCKET", "Done disconnected")
                                    socketClient.disconnect()
                                }
                                Log.d(TAG, "socketListener: ${conn.response}")

                            } else {

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
                                recycerlViewAdapter?.notifyItemInserted(5)
                                binding!!.done.text = getText(R.string.done)
                                binding!!.progressBarCyclic.visibility = View.GONE
                                binding!!.done.isEnabled = true
                                binding!!.done.isClickable = true

                            }




                      //  isFirstTime = true

                    } catch (e: Exception) {
                        Log.d(TAG, "socketListener: ${e.message}")
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        Log.d(TAG, "socketListener: ${e.message}")
                    }
                }

                is ConnectionResponse.OnResponseError -> {
                    Log.d(TAG, "socketListener:OnResponseError $conn")
                }
            }

        }

    }


    override fun returnValue(
        title: String?,
        finalvalue: String?,
        position: Int,
        operation: String?,
        elevation: String?
    ) {
    }

}