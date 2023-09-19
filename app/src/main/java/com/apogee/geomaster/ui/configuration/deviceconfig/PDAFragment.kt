package com.apogee.geomaster.ui.configuration.deviceconfig

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentCorrectionBinding
import com.apogee.geomaster.databinding.FragmentPdaBinding
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
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.concurrent.atomic.AtomicBoolean

class PDAFragment : Fragment(R.layout.fragment_pda), OnItemValueListener {
    private lateinit var binding: FragmentPdaBinding
    private val dbTask by lazy {
        DatabaseRepsoitory(requireContext())
    }
    private val args by navArgs<PDAFragmentArgs>()
    private lateinit var socketClient: SocketClient




    var dgps_id = 0
    var motherBoardID = 0
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
    var recycerlViewAdapter: RecyclerlViewAdapter? = null
    var TypeList: ArrayList<ItemType> = ArrayList()
    var map1: LinkedHashMap<String, String> = LinkedHashMap()
    var listDataSource: ArrayList<HashMap<String, String>> = ArrayList()
    private var working = AtomicBoolean(true)
    private var socket: Socket? = null
    private var dataInputStream: DataInputStream? = null
    private var dataOutputStream: DataOutputStream? = null
    val TAG = "PDAFragment"
    var IP = ""
    var Port = ""
    var dataRequest = ""
    var MountPoint = ""
    var inputParam = ""
    var isFirstTimeSocket = true
    private val sharedPreferences by lazy {
        MyPreference.getInstance(requireActivity())
    }
    var data = "\nGET / HTTP/1.0\n" +
            "User-Agent: NTRIP ApogeeGnss\n" +
            "Accept: */*\n" +
            "Connection: close\r\n\r\n"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentPdaBinding.bind(view)



        gnssmodulename = args.gnssModuleName
        val dgpsid: String? = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID)
        val mthrBrd: String? = sharedPreferences.getStringData(Constants.MOTHERBOARDID)
        if (dgpsid != null) {
            dgps_id = dgpsid.toInt()
            motherBoardID=mthrBrd!!.toInt()
        }



        val opppid = dbTask.getOperationId(getString(R.string.pda))
        radiocommands.clear()
        radiodelay.clear()
        commandsfromlist.clear()
        delaylist.clear()
        editpoint(opppid)

        binding!!.refreshSSID.visibility = View.GONE
        binding!!.idBtnToggle.visibility = View.GONE

        binding.done.setOnClickListener{
            binding.done.requestFocus()
            for (key in mapParameters.keys) {
                Log.d("paramName", key)
            }

            for (key in map1.keys) {
                Log.d("paramName==", key)
            }
            CorrectionFragment.roverMap = map1
            if (mapParameters.keys == map1.keys) {

                if (binding!!.done.text == getText(R.string.done)) {
                    if (map1.containsKey("Mount-Point")) {
                        MountPoint = Conversion(requireContext()).hextToString(map1["Mount-Point"]!!).toString()

                    }
                    val id = dbTask.getidDataSource()
                    var dataSourceId = id.toInt()
                    dataSourceId++
                    for ((key, value) in map1) {
                        dbTask.insertdataSorcestable(
                            getString(R.string.pda),
                            key,
                            value,
                            dataSourceId.toString(),
                            "",
                            gnssmodulename
                        )
                    }
findNavController().popBackStack()
                }
                else if (binding.done.text == getText(R.string.get_mount_point)) {
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
                            getMountPoint(IP,Port)
                        }

                    }
                }

            } else {
                requireActivity().toastMsg(getString(R.string.please_fill_all_data))
            }
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


    fun editpoint(opid: Int) {
        var selectionV: Map<String, String>
        recycerlViewAdapter = RecyclerlViewAdapter(TypeList, this)
        binding!!.recyclerView.adapter = recycerlViewAdapter
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.layoutManager = mLayoutManager
        val commandls: List<Int?>
        commandls = dbTask.commandidls1(opid, motherBoardID)
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
                    dbTask.getDataSource(getString(R.string.pda), count.toString(), gnssmodulename)
                Log.d("TAG", "showPDAData: " + dataSourceList)
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
                inputparameterlist = dbTask.inputparameterlist(joined3) as ArrayList<String>
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
                inputparameterlist = dbTask.inputparameterlists(joined3) as ArrayList<String>
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




    override fun returnValue(title: String?, finalvalue: String?) {
        if (!finalvalue.equals("--select--", ignoreCase = true)) {
            val titl = title!!
            val returnname = finalvalue!!

            map1[titl] = returnname


        }    }

    override fun returnValue(
        title: String?,
        finalvalue: String?,
        position: Int,
        operation: String?,
        elevation: String?
    ) {
    }

}