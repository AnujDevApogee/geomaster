package com.apogee.geomaster.ui.configuration

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConfigurationListAdapter
import com.apogee.geomaster.adaptor.ProjectListAdaptor
import com.apogee.geomaster.databinding.ConfigurationFragmentLayoutBinding
import com.apogee.geomaster.model.ConfigSetup
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox
import com.google.android.material.transition.MaterialFadeThrough

class ConfigurationFragment : Fragment(R.layout.configuration_fragment_layout) {


    private lateinit var binding:ConfigurationFragmentLayoutBinding
    private lateinit var myPreference : MyPreference
    private lateinit var configListAdaptor: ConfigurationListAdapter
    private lateinit var dbControl: DatabaseRepsoitory
    var configListData : ArrayList<String> = ArrayList()






    private val recycleAdaptorCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            Log.i("TAG", "onClickListener: itemclick_project")
            if (response is ConfigSetup){
                activity?.setUpDialogBox("Information","Continue with ${response.configurationName}","Continue","Cancel", success = {
                    myPreference.putStringData("Last_Used_config",response.configurationName)
                    Log.i("TAG", "onClickListener: LastUsed_saved -> ${myPreference.getStringData("Last_Used_config")}")
                    findNavController().navigate(R.id.action_configurationFragment_to_projectListFragment)
                }, cancelListener = {

                })

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fadeThrough = MaterialFadeThrough().apply {
            duration = 1000
        }

        enterTransition = fadeThrough
        reenterTransition = fadeThrough
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConfigurationFragmentLayoutBinding.bind(view)
        myPreference=MyPreference.getInstance(requireContext())
        dbControl = DatabaseRepsoitory(this.requireContext())

        displayActionBar("Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        setUpRecycleView()
        binding.addProject.setOnClickListener {
            findNavController().safeNavigate(ConfigurationFragmentDirections.actionConfigurationFragmentToCreateConfigurationFragment())
        }
        val value= myPreference.getStringData("Last_Used_config")
        configListData= dbControl.getConfigurationList() as ArrayList<String>
        val configDetails : ArrayList<ConfigSetup> = ArrayList()
        Log.d("TAG", "onViewCreated: configListData--$configListData")
        for(i in configListData){
            val configurationName=i.split(",")[0]
            val datumName= i.split(",")[1]
            val workMode= "idle"
            /*       var  zone="44"
                   var  projectionType="UTM"*/
            configDetails.add(ConfigSetup(configurationName,datumName,workMode))
        }

        val obj=configDetails.find {
            it.configurationName==value
        }
        val ls= mutableListOf<ConfigSetup>()
        obj?.let {
            ls.add(obj)
            configDetails.remove(obj)
        }
        ls.addAll(configDetails)
        configListAdaptor.submitList(ls)


    }


    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            findNavController().navigate(R.id.action_projectListFragment_to_configurationFragment)
        }
    }

    private fun setUpRecycleView() {
        binding.recycleViewProject.apply {
            configListAdaptor = ConfigurationListAdapter(recycleAdaptorCallback)
            adapter = configListAdaptor
        }
    }


}