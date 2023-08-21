package com.apogee.geomaster.ui.configuration.satellite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ProjectListAdaptor
import com.apogee.geomaster.adaptor.SatelliteScreenAdaptor
import com.apogee.geomaster.databinding.SatelliteConfigurationFragmentBinding
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.model.SatelliteModel
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate

class SatelliteConfigurationFragment : Fragment(R.layout.satellite_configuration_fragment) {

    private lateinit var binding: SatelliteConfigurationFragmentBinding

    private lateinit var adaptor: SatelliteScreenAdaptor
    private lateinit var dbControl: DatabaseRepsoitory

    var satelliteDetails : ArrayList<SatelliteModel> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbControl = DatabaseRepsoitory(this.requireContext())
        binding = SatelliteConfigurationFragmentBinding.bind(view)
        displayActionBar("Satellite Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        setRecycleView()
        val satelliteList=dbControl.getSatelliteDataList()
        for(i in satelliteList!!){
            satelliteDetails.add(SatelliteModel(i))
        }
        adaptor.submitList(satelliteDetails)
        binding.doneBtn.setOnClickListener {
            findNavController().safeNavigate(R.id.action_satelliteConfigurationFragment_to_deviceConfiguration)
        }
    }

    private fun setRecycleView() {
        binding.recycleView.apply {

            this@SatelliteConfigurationFragment.adaptor = SatelliteScreenAdaptor {

            }


            adapter = this@SatelliteConfigurationFragment.adaptor
            this@SatelliteConfigurationFragment.adaptor.submitList(satelliteDetails)
        }
    }
}