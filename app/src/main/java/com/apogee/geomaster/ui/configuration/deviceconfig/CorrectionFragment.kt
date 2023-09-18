package com.apogee.geomaster.ui.configuration.deviceconfig

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.DataSourceAdapter
import com.apogee.geomaster.databinding.FragmentCorrectionBinding
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.ui.configuration.deviceconfig.rover.GnssRoverProfileFragment
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.toastMsg


class CorrectionFragment : Fragment(R.layout.fragment_correction) {

    private lateinit var binding: FragmentCorrectionBinding
    private val args by navArgs<CorrectionFragmentArgs>()
    private lateinit var dbControl: DatabaseRepsoitory

    val TAG = "CorrectionFragment"
    var gnssmodulename = ""
    var whichRtk = ""

    var dgps_id = 0
    var sharedPreferences : MyPreference? = null
    var listDataSource : ArrayList<HashMap<String,String>> = ArrayList()
    var deviceModuleList:ArrayList<String> = ArrayList();
    companion object{
        var roverMap: HashMap<String, String> = HashMap()
        var radioMap : HashMap<String, String> = HashMap()
        var externalRadioMap : HashMap<String, String> = HashMap()
        var wifiMap : HashMap<String, String> = HashMap()
        var pdaMap : HashMap<String, String> = HashMap()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCorrectionBinding.bind(view)
        Log.d(TAG, "onViewCreated: ${args.mode}")
        dbControl = DatabaseRepsoitory(requireContext())


        roverMap.clear()
        radioMap.clear()
        wifiMap.clear()
        pdaMap.clear()
        externalRadioMap.clear()
        sharedPreferences = MyPreference.getInstance(requireContext())
        val deviceName=sharedPreferences!!.getStringData(Constants.DEVICE_NAME)
        val moduleDevice=sharedPreferences!!.getStringData(Constants.MODULE_DEVICE)
        Log.d(TAG, "onViewCreated: $moduleDevice")
//        val moduleDevice="3, 9, 10, 13, 15, 6, 16"
        val dgpsid = sharedPreferences!!.getStringData(Constants.DGPS_DEVICE_ID)
        if (dgpsid != null && dgpsid.isNotEmpty()) {
            dgps_id = dgpsid.toInt()
        }
        Log.d("TAG", "onCreate:moduleDevice "+moduleDevice)
        gnssmodulename = args.mode
        if(gnssmodulename.equals(getString(R.string.rover)) && deviceName.contains("NAVIK200-1.2"))
        {
            binding!!.cvPDA.visibility=View.VISIBLE
        } else
        {
            binding!!.cvPDA.visibility=View.GONE
        }
        if(moduleDevice!!.contains("9"))
        {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            whichRtk = getString(R.string.gsm)
            binding!!.gnssreference.visibility=View.VISIBLE
            binding!!.datasource.visibility=View.VISIBLE
            binding!!.btwifi.visibility=View.GONE
            binding!!.communication.visibility=View.GONE
            binding!!.radioGroup.visibility=View.GONE

            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test1)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue1)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue1)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
            showGsmData()
            deviceModuleList.add("9")
        }
        if(moduleDevice!!.contains("10"))
        {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            binding!!.cvWifi.visibility=View.VISIBLE
            binding!!.btwifi.visibility=View.VISIBLE
            binding!!.communication.visibility=View.GONE
            binding!!.datasource.visibility=View.GONE
            binding!!.radioGroup.visibility=View.GONE

            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test1)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue1)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue1)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
            showWifiData()
            deviceModuleList.add("10")
            whichRtk = getString(R.string.wifi)

        }
        if(moduleDevice!!.contains("13"))
        {
            deviceModuleList.add("13")
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            binding!!.gnssrover.visibility=View.VISIBLE
            binding!!.communication.visibility=View.VISIBLE
            binding!!.btwifi.visibility=View.GONE
            binding!!.datasource.visibility=View.GONE
            binding!!.radioGroup.visibility=View.GONE
            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test1)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue1)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue1)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)

             whichRtk = getString(R.string.radio)

            showRadioData()
        }
        if(moduleDevice!!.contains("15"))
        {
            binding!!.radioGroup.visibility=View.VISIBLE
            /* deviceModuleList.add("15")
             roverMap.clear()
             radioMap.clear()
             wifiMap.clear()
             pdaMap.clear()
             externalRadioMap.clear()
             binding!!.externalRadio.visibility=View.VISIBLE
             binding!!.btnExternalRadio.visibility=View.VISIBLE
             binding!!.btwifi.visibility=View.GONE
             binding!!.datasource.visibility=View.GONE
             binding!!.communication.visibility=View.GONE
             val colorValueblue = ContextCompat.getColor(this, R.color.test)
             val colorValueblue1 = ContextCompat.getColor(this, R.color.test1)
             binding!!.gnssrover.setCardBackgroundColor(colorValueblue1)
             binding!!.gnssreference.setCardBackgroundColor(colorValueblue1)
             binding!!.externalRadio.setCardBackgroundColor(colorValueblue)
             binding!!.cvWifi.setCardBackgroundColor(colorValueblue1)
             binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
             showRadioData()
             whichRtk = getString(R.string.radio_external)*/
        }
        if(deviceModuleList.size==1)
        {
            if(deviceModuleList.contains("9"))
            {
                binding!!.gnssreference.visibility=View.GONE
            }
            if(deviceModuleList.contains("10"))
            {
                binding!!.cvWifi.visibility=View.GONE
            }
            if(deviceModuleList.contains("13"))
            {
                binding!!.gnssrover.visibility=View.GONE
            }

        }
        //  whichRtk = getString(R.string.gsm)

        binding!!.gnssreference.setOnClickListener{
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test1)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue1)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue1)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
            binding!!.communication.visibility=View.GONE
            binding!!.datasource.visibility=View.VISIBLE
            binding!!.btPDA.visibility=View.GONE
            binding!!.btwifi.visibility = View.GONE
            binding!!.radioGroup.visibility = View.GONE
            showGsmData()
            whichRtk = getString(R.string.gsm)
        }

        binding!!.gnssrover.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test1)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue1)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue)
            binding!!.communication.visibility=View.VISIBLE
            binding!!.btPDA.visibility=View.GONE
            binding!!.datasource.visibility=View.GONE
            binding!!.btwifi.visibility = View.GONE
            binding!!.radioGroup.visibility = View.VISIBLE

            showRadioData()
            whichRtk = getString(R.string.radio)

        }


        binding!!.cvWifi.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test1)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue1)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue)
            binding!!.communication.visibility = View.GONE
            binding!!.datasource.visibility = View.GONE
            binding!!.btPDA.visibility = View.GONE
            binding!!.btwifi.visibility = View.VISIBLE
            binding!!.radioGroup.visibility = View.GONE
            showWifiData()
            whichRtk = getString(R.string.wifi)
        }

        binding!!.cvPDA.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            val colorValueblue = ContextCompat.getColor(requireContext(), R.color.test1)
            val colorValueblue1 = ContextCompat.getColor(requireContext(), R.color.test)
            binding!!.cvPDA.setCardBackgroundColor(colorValueblue1)
            binding!!.gnssreference.setCardBackgroundColor(colorValueblue)
            binding!!.gnssrover.setCardBackgroundColor(colorValueblue)
            binding!!.cvWifi.setCardBackgroundColor(colorValueblue)
            binding!!.communication.visibility = View.GONE
            binding!!.datasource.visibility = View.GONE
            binding!!.btwifi.visibility = View.GONE
            binding!!.btPDA.visibility = View.VISIBLE
            binding!!.radioGroup.visibility = View.GONE
            showPDAData()
            whichRtk = getString(R.string.pda)
        }

        binding!!. done.setOnClickListener {
            Log.d("TAG", "onCreate: "+pdaMap)
            if(roverMap.size > 0 || radioMap.size > 0 || wifiMap.size > 0 || pdaMap.size > 0||  externalRadioMap.size > 0){
                GnssRoverProfileFragment.radioMapProfile = radioMap
                GnssRoverProfileFragment.roverMapProfile = roverMap
                GnssRoverProfileFragment.wifiMapProfile = wifiMap
                GnssRoverProfileFragment.pdaMapProfile = pdaMap
                GnssRoverProfileFragment.externalradioMapProfile = externalRadioMap
 /*               BaseConfigActivity.radioMapProfile = radioMap
                BaseConfigActivity.externalRadioMapProfile = externalRadioMap
                BaseConfigActivity.roverMapProfile = roverMap
                BaseConfigActivity.wifiMapProfile = wifiMap*/
                findNavController().safeNavigate(CorrectionFragmentDirections.actionGlobalGnssRoverProfileFragment())
            }else{
               requireActivity().toastMsg(getString(R.string.please_set_up_correction))
            }
            binding!!.done.isClickable = false
        }

        binding!!.datasource.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            externalRadioMap.clear()
            wifiMap.clear()
            pdaMap.clear()

            val newConnectionSource = NewConnectionSourceFragment()
          findNavController().safeNavigate(CorrectionFragmentDirections.actionCorrectionFragmentToNewConnectionSourceFragment2(gnssmodulename))
            binding!!.datasource.isClickable = false
        }

        binding!!.communication.setOnClickListener {

            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            sharedPreferences!!.putStringData(Constants.RADIO_TYPE,whichRtk)
            findNavController().safeNavigate(CorrectionFragmentDirections.actionCorrectionFragmentToRadioCommunication2(gnssmodulename,whichRtk))
            binding!!.communication.isClickable = false
        }

        binding!!.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(binding!!.rbRadioExternal.isChecked)
            {
                whichRtk=resources.getString(R.string.radio_external)
                showExternalRadio()
            }else
            {
                    whichRtk=resources.getString(R.string.radio)
                showRadioData()
            }
        }

        binding!!.btwifi.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
        findNavController().safeNavigate(CorrectionFragmentDirections.actionCorrectionFragmentToWiFiSetupFragment2(gnssmodulename))
            binding!!.btwifi.isClickable = false
        }

        binding!!.btPDA.setOnClickListener {
            roverMap.clear()
            radioMap.clear()
            wifiMap.clear()
            pdaMap.clear()
            externalRadioMap.clear()
            findNavController().safeNavigate(CorrectionFragmentDirections.actionCorrectionFragmentToPDAFragment(gnssmodulename))
//            intent.putExtra(Constants.GNSSMODULENAME, gnssmodulename)
//            startActivity(intent)
            binding!!.btPDA.isClickable = false
        }



    }



    fun showGsmData(){
        listDataSource.clear()
        var count = 1
        val id =  dbControl.getidDataSource()
        for (i in 0 until id.toInt()){
            if(count <= id.toInt()){
                val dataSourceList = dbControl.getDataSource(getString(R.string.gsm),count.toString(),gnssmodulename)
                if(dataSourceList!!.isNotEmpty()){
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }
        val  dataSourceAdapter = DataSourceAdapter(requireContext())
        dataSourceAdapter.setAdapter(requireContext(),listDataSource,false)
        dataSourceAdapter.setListerner(object : DataSourceAdapter.ClickListerner{
            override fun onSuccess(pos: Int) {
                val dataSource = listDataSource[pos]
                roverMap = dataSource
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(id: String?) {
                val result: Boolean = dbControl.deletedataSource(id!!)
            }

        })
        binding!!.recyclerView.adapter = dataSourceAdapter
    }
    fun showPDAData(){
        listDataSource.clear()
        var count = 1
        val id =  dbControl.getidDataSource()
        for (i in 0 until id.toInt()){
            if(count <= id.toInt()){
                val dataSourceList = dbControl.getDataSource(getString(R.string.pda),count.toString(),gnssmodulename)
                if(dataSourceList!!.isNotEmpty()){
                    listDataSource.add(dataSourceList)
                }

                count++
            }
        }
        Log.d("TAG", "showPDAData: "+listDataSource)
        val  dataSourceAdapter = DataSourceAdapter(requireContext())
        dataSourceAdapter.setAdapter(requireContext(),listDataSource,false)
        dataSourceAdapter.setListerner(object : DataSourceAdapter.ClickListerner{
            override fun onSuccess(pos: Int) {
                val dataSource = listDataSource[pos]
                pdaMap = dataSource
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(id: String?) {
                val result: Boolean = dbControl.deletedataSource(id!!)
            }

        })
        binding!!.recyclerView.adapter = dataSourceAdapter
    }
    fun showExternalRadio(){
        listDataSource.clear()
        var count = 1
        val id =  dbControl.getidDataSource()
        for (i in 0 until id.toInt()){
            if(count <= id.toInt()){
                val dataSourceList = dbControl.getDataSource(getString(R.string.radio_external),count.toString(),gnssmodulename)
                if(dataSourceList!!.isNotEmpty()){
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }
        Log.d("TAG", "showExternalRadio: "+listDataSource)
        val  dataSourceAdapter = DataSourceAdapter(requireContext())
        dataSourceAdapter.setAdapter(requireContext(),listDataSource,false)
        dataSourceAdapter.setListerner(object : DataSourceAdapter.ClickListerner{
            override fun onSuccess(pos: Int) {
                val dataSource = listDataSource[pos]
                externalRadioMap = dataSource
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(id: String?) {
                val result: Boolean = dbControl.deletedataSource(id!!)
            }

        })
        binding!!.recyclerView.adapter = dataSourceAdapter
    }
    fun showRadioData(){
        listDataSource.clear()
        var count = 1
        val id =  dbControl.getidDataSource()
        for (i in 0 until id.toInt()){
            if(count <= id.toInt()){
                val dataSourceList = dbControl.getDataSource(whichRtk,count.toString(), gnssmodulename)
                if(dataSourceList!!.isNotEmpty()){
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }
        Log.d(TAG, "showRadioData: listDataSource--$listDataSource")

        val  dataSourceAdapter = DataSourceAdapter(requireContext())
        dataSourceAdapter.setAdapter(requireContext(),listDataSource,true)
        dataSourceAdapter.setListerner(object : DataSourceAdapter.ClickListerner{
            override fun onSuccess(pos: Int) {
                val dataSource = listDataSource[pos]
                radioMap = dataSource
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(id: String?) {
                val result: Boolean = dbControl.deletedataSource(id!!)
            }

        })
        binding!!.recyclerView.adapter = dataSourceAdapter
    }
    fun showWifiData(){
        listDataSource.clear()
        var count = 1
        val id =  dbControl.getidDataSource()
        for (i in 0 until id.toInt()){
            if(count <= id.toInt()){
                val dataSourceList = dbControl.getDataSource(getString(R.string.wifi),count.toString(),gnssmodulename)
                Log.d("TAG", "showWifiData: "+dataSourceList)
                if(dataSourceList!!.isNotEmpty()){
                    listDataSource.add(dataSourceList)
                }
                count++
            }
        }
        val  dataSourceAdapter = DataSourceAdapter(requireContext())
        dataSourceAdapter.setAdapter(requireContext(),listDataSource,true)
        dataSourceAdapter.setListerner(object : DataSourceAdapter.ClickListerner{
            override fun onSuccess(pos: Int) {
                val dataSource = listDataSource[pos]
                wifiMap = dataSource
                Log.d("TAG", "onSuccess: "+ wifiMap)
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDelete(id: String?) {
                val result: Boolean = dbControl.deletedataSource(id!!)
            }

        })
        binding!!.recyclerView.adapter = dataSourceAdapter
    }

}