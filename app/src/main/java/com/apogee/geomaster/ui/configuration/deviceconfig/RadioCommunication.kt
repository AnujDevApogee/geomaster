package com.apogee.geomaster.ui.configuration.deviceconfig

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityRadioCommunicationBinding
import com.apogee.geomaster.model.MultiView.ItemType
import com.apogee.geomaster.model.MultiView.OnItemValueListener
import com.apogee.geomaster.model.MultiView.RecyclerlViewAdapter

import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.Conversion
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.toastMsg
import com.google.gson.Gson
import java.util.*

class RadioCommunication : Fragment(R.layout.activity_radio_communication), OnItemValueListener {

    var binding: ActivityRadioCommunicationBinding? = null
    private lateinit var dbControl: DatabaseRepsoitory
    private val args by navArgs<RadioCommunicationArgs>()

    var radiocommands: ArrayList<String> = ArrayList()
    var radiodelay: ArrayList<String> = ArrayList()
    var commandsfromlist: ArrayList<String> = ArrayList()
    var delaylist: ArrayList<String> = ArrayList()
    var gnssdelay: ArrayList<String> = ArrayList()
    var gnsscommands: ArrayList<String> = ArrayList()
    var gnnsFormatCommands: ArrayList<String> = ArrayList()
    var radioFormatCommands: ArrayList<String> = ArrayList()
    var dgps_id = 0
    var motherBoardID = 0
    var map1: HashMap<String, String> = HashMap()
    var gnssmodulename = ""
    var value = 0
    var currentValue = 0
    var temp_device_name=""

    var protocolKey: ArrayList<String> = ArrayList()
    var protocolValue: ArrayList<String> = ArrayList()
    var rs232Key: ArrayList<String> = ArrayList()
    var rs232Value: ArrayList<String> = ArrayList()


//    var protocolKey: ArrayList<String> = ArrayList()
//    var protocolValue: ArrayList<String> = ArrayList()


    lateinit var sharedPreferences: MyPreference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbControl = DatabaseRepsoitory(requireContext())
        binding =ActivityRadioCommunicationBinding.bind(view)
        sharedPreferences = MyPreference.getInstance(requireContext())
        val dgpsid: String = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID)
        val mthrBrd: String = sharedPreferences.getStringData(Constants.MOTHERBOARDID)
        temp_device_name  = sharedPreferences.getStringData(Constants.DEVICE_NAME)
        val radiodgps_id: String? = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID_FOR_RADIO)
        val radioType=args.radioRtkType
//        val radioType=resources.getString(R.string.radio)
        Log.d("TAG", "onCreateradioGroup: "+radioType)

        if (dgpsid != null) {
            dgps_id = dgpsid.toInt()
            motherBoardID = mthrBrd.toInt()
        }

        if(temp_device_name.contains("NAVIK300") && radioType.equals(resources.getString(R.string.radio))){
            binding!!.llFrequency.visibility = View.VISIBLE
        }

        binding!!.firstThreeDigitsEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().trim { it <= ' ' }.isEmpty()) {
                    if (s.toString().length == 3) {
                        binding!!.lastFourDigitsEditText.setEnabled(true)
                    } else {
                        requireActivity().toastMsg("Please enter the first three digits")
                        binding!!.lastFourDigitsEditText.setEnabled(false)
                    }
                }
            }
        })


        binding!!.add.setOnClickListener{
            if (!TextUtils.isEmpty(binding!!.firstThreeDigitsEditText.getText()) && binding!!.firstThreeDigitsEditText.getText().get(0) == '4') {
                currentValue = binding!!.firstThreeDigitsEditText.getText().toString().toInt()
                if (currentValue != 0) {
                    if (value <= 875) {
                        value += 125
                        if (value > 875) {
                            value = 125
                            val newValue: Int = currentValue + 1
                            binding!!.firstThreeDigitsEditText.setText(newValue.toString())
                            binding!!.lastFourDigitsEditText.setText("0$value")
                        } else {
                            binding!!.lastFourDigitsEditText.setText("0$value")
                        }
                    } else {
                        requireActivity().toastMsg("Maximum value reached")
                    }
                } else {
                    requireActivity().toastMsg("Please enter the first three digits")
                }
            } else {
                requireActivity().toastMsg("Value does not start with digit 4 or is empty")
            }
        }

        binding!!.sub.setOnClickListener{
            if (!TextUtils.isEmpty(binding!!.firstThreeDigitsEditText.getText()) && binding!!.firstThreeDigitsEditText.getText().get(0) == '4') {
                currentValue = binding!!.firstThreeDigitsEditText.getText().toString().toInt()
                if (currentValue != 0) {
                    if (value >= 125) {
                        value -= 125
                        if (value <= 0) {
                            value = 875
                            val newValue: Int = currentValue - 1
                            binding!!.firstThreeDigitsEditText.setText(newValue.toString())
                            binding!!.lastFourDigitsEditText.setText("0$value")
                        } else {
                            binding!!.lastFourDigitsEditText.setText("0$value")
                        }
                    } else {
                        requireActivity().toastMsg("Minimum value reached")
                    }
                } else {
                    requireActivity().toastMsg("Please enter the first three digits")
                }
            } else {
                requireActivity().toastMsg("Value does not start with digit 4 or is empty")
            }
        }

        val  opppid = dbControl.getOperationId(radioType)
        radiocommands.clear()
        radiodelay.clear()
        commandsfromlist.clear()
        delaylist.clear()
        editpoint(opppid)
        gnssmodulename = args.gnssModuleName
        binding!!.confirm.setOnClickListener {
            binding!!.confirm.requestFocus()
            /*REQUEST FOCUS */


            if(temp_device_name.contains("NAVIK300") && radioType.equals(resources.getString(R.string.radio))) {
                if (!TextUtils.isEmpty(binding!!.firstThreeDigitsEditText.getText())&&
                    binding!!.firstThreeDigitsEditText.getText().get(0) == '4') {
                    val firstThreeValue: String = binding!!.firstThreeDigitsEditText.getText().toString()
                    val lastFourValue: String = binding!!.lastFourDigitsEditText.getText().toString()
                    val finalValue = firstThreeValue + lastFourValue
                    currentValue = finalValue.toInt()
                    if (firstThreeValue.length == 3) {
                        if (currentValue >= 4100000 && currentValue <= 4700000) {
                            var temp_remark: Int = (currentValue % 125).toInt()
                            if (temp_remark == 0) {
                                map1.put("Frequency", Conversion(requireContext()).stringtohex(currentValue.toString()))
                                val id = dbControl.getidDataSource()
                                var dataSourceId = id.toInt()
                                dataSourceId++
                                if (radioType.equals(resources.getString(R.string.radio))) {
                                    CorrectionFragment.radioMap = map1
                                    val id = dbControl.getidDataSource()
                                    var dataSourceId = id.toInt()
                                    dataSourceId++
                                    for ((key, value) in map1) {
                                        dbControl.insertdataSorcestable(getString(R.string.radio), key, value, dataSourceId.toString(), "", gnssmodulename)
                                    }
                                    requireActivity().supportFragmentManager.popBackStack()
                                } else {
                                    CorrectionFragment.externalRadioMap = map1
                                    val id = dbControl.getidDataSource()
                                    var dataSourceId = id.toInt()
                                    dataSourceId++
                                    for ((key, value) in map1) {
                                        dbControl.insertdataSorcestable(getString(R.string.radio_external), key, value, dataSourceId.toString(), "", gnssmodulename)
                                    }
                                    requireActivity().supportFragmentManager.popBackStack()
                                }
                                Log.d(TAG, "onCreateConfirm1: " + map1)
                            } else {
                               requireActivity().toastMsg("Please check the frequency Value")
                            }
                        } else {
                            requireActivity().toastMsg("Please enter frequency between 4100000 to 4700000 ")
                        }
                    } else {
                        requireActivity().toastMsg("Please enter the first three digits")
                    }
                } else {
                    requireActivity().toastMsg("Value does not start with digit 4 or is empty")
                }
            }
            else
            {
                Log.d(TAG, "onViewCreated: radioType--$radioType")
                if(radioType.equals(resources.getString(R.string.radio)))
                {

                    CorrectionFragment.radioMap = map1
                    val id =  dbControl.getidDataSource()
                    var dataSourceId = id.toInt()
                    dataSourceId++
                    for ((key, value) in map1) {
                        dbControl.insertdataSorcestable(getString(R.string.radio), key, value,dataSourceId.toString(),"", gnssmodulename)
                    }
                    requireActivity().supportFragmentManager.popBackStack()
                }else
                {
                    CorrectionFragment.externalRadioMap = map1
                    Log.d(TAG, "onCreateExternalRadioMap: "+map1)
                    val id =  dbControl.getidDataSource()
                    var dataSourceId = id.toInt()
                    dataSourceId++
                    for ((key, value) in map1) {
                        dbControl.insertdataSorcestable(getString(R.string.radio_external), key, value,dataSourceId.toString(),"", gnssmodulename)
                    }
                    requireActivity().supportFragmentManager.popBackStack()
//                    requireActivity().finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
return true       }
        return true
    }



    @SuppressLint("NotifyDataSetChanged")
    fun editpoint(opid: Int) {

        val recycerlViewAdapter: RecyclerlViewAdapter
        var selectionV: Map<String, String>
        val TypeList: MutableList<ItemType> = ArrayList()
         recycerlViewAdapter = RecyclerlViewAdapter(TypeList, this)
         binding!!.recyclerView.adapter = recycerlViewAdapter
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.layoutManager = mLayoutManager
        val commandls: List<Int?>
        commandls = dbControl.commandidls1(opid, motherBoardID)
        val joined = TextUtils.join(", ", commandls)
        var parameteridlist = ArrayList<Int?>()
        parameteridlist = dbControl.parameteridlist(joined) as ArrayList<Int?>
        val selectionidlist: ArrayList<Int?>
        selectionidlist = dbControl.selectionidlist1(joined) as ArrayList<Int?>
        val joined2 = TextUtils.join(", ", selectionidlist)

        /*View for selection recyclerview*/
        val inputlist: ArrayList<Int?>
        inputlist = dbControl.inputlist(joined) as ArrayList<Int?>
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
            if(param.equals("Protocol")){
                protocolKey.addAll(baudratekey)
                protocolValue.addAll(baudratevalue)
//                TypeList.add(ItemType(ItemType.DROPDOWNTYPE, param, selectionV))
            }
            else if(param.equals("RS232 Baudrate")){
                rs232Key.addAll(baudratekey)
                rs232Value.addAll(baudratevalue)
//                TypeList.add(ItemType(ItemType.DROPDOWNTYPE, param, selectionV))
            }
            else{
               TypeList.add(ItemType(ItemType.DROPDOWNTYPE, param, selectionV))
                 recycerlViewAdapter.notifyDataSetChanged()
            }

            println("Initial values : ${TypeList.size}") //
        }

        if(protocolKey.size>0)
        {
            Log.d(TAG, "editpointProtocol: "+protocolKey+"\n"+protocolValue)
            val gson = Gson()
            val json1: String = gson.toJson(protocolKey)
            val json2: String = gson.toJson(protocolValue)
            sharedPreferences.putStringData(Constants.TRIMBLE_ProtocolKey, json1)
            sharedPreferences.putStringData(Constants.TRIMBLE_ProtocolValue, json2)
        }
         if(rs232Key.size>0)
        {
            Log.d(TAG, "editpoint: "+rs232Key)
            val gson = Gson()
            val json1: String = gson.toJson(rs232Key)
            val json2: String = gson.toJson(rs232Value)
            sharedPreferences.putStringData(Constants.TRIMBLE_rs232Key, json1)
            sharedPreferences.putStringData(Constants.TRIMBLE_rs232Value, json2)
        }

        /*View for Input recyclerview*/
        if (gnssmodulename == getString(R.string.rover)) {
            val inputparameterlist: ArrayList<String>
            inputparameterlist = dbControl.inputparameterlist(joined3) as ArrayList<String>
            for (inputparam in inputparameterlist) {
                val type = inputparam.split(",".toRegex()).toTypedArray()[1]
                if (type != "String") {
                    TypeList.add(ItemType(ItemType.INPUTTYPE, inputparam.split(",".toRegex()).toTypedArray()[0], null, null, null, null,null,null,false,false))
                    recycerlViewAdapter.notifyDataSetChanged()
                } else if (type == "String") {
                    val url = inputparam.split(",".toRegex()).toTypedArray()[2]
                    //  getMountPoint(url, TypeList, inputparam.split(",".toRegex()).toTypedArray()[0], recycerlViewAdapter)
                }
            }
        } else {
            val inputparameterlist: ArrayList<String>
            inputparameterlist = dbControl.inputparameterlists(joined3)
            for (inputparam in inputparameterlist) {
                TypeList.add(ItemType(ItemType.INPUTTYPE, inputparam, null, null, null, null,null,null,false,false))
                recycerlViewAdapter.notifyDataSetChanged()
            }
        }


    }

    override fun returnValue(title: String?, finalvalue: String?) {
        if (!finalvalue.equals("--select--", ignoreCase = true)) {
            val titl = title!!
            val value = finalvalue!!
            Log.d(TAG, "returnValue: "+finalvalue)
            if(titl.equals("Protocol")&&temp_device_name.contains("NAVIK300")){
                val temp_key =protocolValue.indexOf(value)
                val temp_value = protocolKey.get(temp_key)
                Log.d(TAG, "returnValue:temp_key "+temp_key+"\n"+temp_value)
                val temp_split: Array<String> = temp_value.split("at").toTypedArray()
                for (part in temp_split) {
                    if (part.contains("bps")) {
                        val valueString = part.replace("[^\\d]".toRegex(), "") // Remove non-digit characters
                        val finalprotocolvalue = valueString.trim().toInt()
                        map1.put("AirDataRate",Conversion(requireContext()).stringtohex(finalprotocolvalue.toString()))
                        println("AirDataRate: $finalprotocolvalue")
                    }
                }
/*                val pattern = Pattern.compile("\\d+")
                val matcher = pattern.matcher(temp_value)
                if (matcher.find()) {
                    val finalprotocolvalue = matcher.group()
                    map1.put("AirDataRate",Utils().stringtohex(finalprotocolvalue.toString()))
                    println("AirDataRateValue=$finalprotocolvalue")
                }*/
            }
            map1.put(titl, finalvalue)
        }

    }

    override fun returnValue(title: String?, finalvalue: String?, position: Int, operation: String?, elevation: String?) {

    }

}