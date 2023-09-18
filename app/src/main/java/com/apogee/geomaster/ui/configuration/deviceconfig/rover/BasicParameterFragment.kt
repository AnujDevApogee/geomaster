package com.apogee.geomaster.ui.configuration.deviceconfig.rover

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentBasicParameterBinding
import com.apogee.geomaster.model.MultiView.ItemType
import com.apogee.geomaster.model.MultiView.OnItemValueListener
import com.apogee.geomaster.model.MultiView.RecyclerlViewAdapter
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.utils.MyPreference
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap


class BasicParameterFragment : Fragment(R.layout.fragment_basic_parameter),OnItemValueListener {

    private lateinit var binding:FragmentBasicParameterBinding
    var dgps_id = 0
    private val sharedPreferences by lazy {
        MyPreference.getInstance(requireActivity())
    }
    var opid = 0
    var commandls1: ArrayList<Int>? = null
    var parameterlist: ArrayList<String>? = null
    var selectionValue1: LinkedHashMap<String, String> = LinkedHashMap()
    var recycerlViewAdapter: RecyclerlViewAdapter? = null
    var itemTypeList: ArrayList<ItemType> = ArrayList()
    var map1: HashMap<String, String> = HashMap()
    val TAG="BasicParameterFragment"
private val dbTask by lazy {
    DatabaseRepsoitory(requireContext())
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBasicParameterBinding.bind(view)

        val dgpsid: String = sharedPreferences.getStringData(Constants.DGPS_DEVICE_ID)
        if (dgpsid.isNotEmpty()) {
            dgps_id = dgpsid.toInt()
        }
        recycerlViewAdapter = RecyclerlViewAdapter(itemTypeList, this)
        binding!!.recyclerView.adapter = recycerlViewAdapter

        val radioType=sharedPreferences.getStringData(Constants.RADIO_TYPE)
        Log.d(TAG, "onViewCreated: radioType --$radioType")
        if(radioType.equals( resources.getString(R.string.radio_external)))
        {
            getcommandid(getString(R.string.radio_external_rover))
        }else
        {
            getcommandid(getString(R.string.rover))
        }


        binding!!.conirm.setOnClickListener {
            binding!!.conirm.requestFocus()
            /*REQUEST FOCUS */

            GnssRoverProfileFragment.parameterList = map1
        findNavController().popBackStack()        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().popBackStack()
        }
        return true
    }

    fun getcommandid(operationname: String?) {

        opid = dbTask.getOperationId(operationname!!)
        commandls1 = ArrayList<Int>()
        parameterlist = ArrayList<String>()
        commandls1 = dbTask.commandidls1(opid, dgps_id)
        val joined: String = TextUtils.join(", ", commandls1!!)
        val parameteridlist: ArrayList<Int>
        parameteridlist = dbTask.parameteridlist(joined)
        val joined1 = TextUtils.join(", ", parameteridlist)

        //All the parameters and their input and selection are parshed here and displayd through recyclerview//
        var parameter_namelist = dbTask.parameternamelist(joined1)

        val selectionidlist = dbTask.selectionidlist(joined)
        val joined2 = TextUtils.join(", ", selectionidlist)
        val inputlist = dbTask.inputlist(joined)
        val joined3 = TextUtils.join(", ", inputlist)
        var selectionValue  = dbTask.getSelection_value_id(joined1, joined)
        val joined4 = TextUtils.join(", ", selectionValue)
        val selectionList = dbTask.displayvaluelist(joined4)
        val selectionParameter = selectionList.keys
        for (param in selectionParameter) {
            selectionValue1 = (selectionList[param] as LinkedHashMap<String, String>?)!!
            if(param.equals("Mask angle")){

                if(selectionValue1.contains("0"))
                {
                    selectionValue1.remove("0")!!
                }
            }
            val baudratekey: Set<String> = selectionValue1.keys
            val baudratevalue: Collection<String> = selectionValue1.values
            val baudratevaluelist: ArrayList<String> = ArrayList(baudratekey)
            baudratevaluelist.add(0, "--select--")
            itemTypeList.add(ItemType(ItemType.DROPDOWNTYPE, param, selectionValue1))
            recycerlViewAdapter?.notifyDataSetChanged()
            println("Initial values : $baudratevalue")
        }
        val inputparameterlist: ArrayList<String>
        inputparameterlist = dbTask.inputparameterlists(joined3)
        for (inputparam in inputparameterlist) {
            itemTypeList.add(ItemType(ItemType.INPUTTYPE, inputparam, null, null, null, null, null,null,false,false))
            recycerlViewAdapter?.notifyDataSetChanged()
        }
    }

    override fun returnValue(title: String?, finalvalue: String?) {
        if (!finalvalue.equals("--select--", ignoreCase = true)) {
            val titl = title!!
            map1[titl] = finalvalue!!
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