package com.apogee.geomaster.ui.configuration.satellite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.SatelliteScreenAdaptor
import com.apogee.geomaster.databinding.SatelliteConfigurationFragmentBinding
import com.apogee.geomaster.model.SatelliteModel
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate

class SatelliteConfigurationFragment : Fragment(R.layout.satellite_configuration_fragment) {

    private lateinit var binding: SatelliteConfigurationFragmentBinding
    private lateinit var adaptor: SatelliteScreenAdaptor
    private lateinit var dbControl: DatabaseRepsoitory


    var satelliteDetails: ArrayList<SatelliteModel> = ArrayList()
    var satelliteStatusList: ArrayList<SatelliteModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbControl = DatabaseRepsoitory(this.requireContext())
        binding = SatelliteConfigurationFragmentBinding.bind(view)
        displayActionBar("Satellite Configuration", binding.actionLayout)
        (activity as HomeScreen?)?.hideActionBar()
        setRecycleView()
        val satelliteList = dbControl.getSatelliteDataList()
        satelliteDetails.clear()
        for (i in satelliteList!!.indices) {
            satelliteDetails.add(
                SatelliteModel(
                    satelliteList.get(i).split(",")[0],
                    satelliteList.get(i).split(",")[1]
                )
            )
            if (satelliteList.get(i).split(",")[1].equals("Y")) {
                satelliteStatusList.add(
                    SatelliteModel(
                        satelliteList.get(i).split(",")[0],
                        satelliteList.get(i).split(",")[1]
                    )
                )
            }
        }

        satelliteDetails.add(SatelliteModel("GLONASS", "N"))
        satelliteDetails.add(SatelliteModel("BDS", "N"))
        satelliteDetails.add(SatelliteModel("GPS", "N"))
        Log.d("TAG", "TEST: TESTsatelliteDetails--$satelliteDetails")
        adaptor.notifyDataSetChanged()
        adaptor.submitList(satelliteDetails)
        binding.doneBtn.setOnClickListener {
            Log.i("LIST_DATA", "onViewCreated: $satelliteDetails")
            var count = 0
            for (i in satelliteDetails) {
                Log.d(
                    "TAG",
                    "onViewCreated:satelliteName ${i.satelliteName + "," + i.satelliteStatus}"
                )
                val result =
                    dbControl.insertSatelliteDataList(i.satelliteName + "," + i.satelliteStatus)
                if (result.equals("")) {
                    count++
                }
                Log.d("TAG", "onViewCreated:Result $result ")
            }
            if (count == 4) {
                findNavController().safeNavigate(R.id.action_satelliteConfigurationFragment_to_deviceConfiguration)
            } else {
                Log.d("TAG", "onViewCreated:count $count ")

            }

        }
    }

    private fun setRecycleView() {
        binding.recycleView.apply {
            this@SatelliteConfigurationFragment.adaptor = SatelliteScreenAdaptor { position, op ->
                Log.d("TAG", "setRecycleView: $op")
                try {
                    satelliteDetails.removeAt(position)
                    satelliteDetails.add(position, op)

                } catch (e: Exception) {
                    Log.d("TAG", "setRecycleView:Exception ${e.message} ")
                }
            }
            adapter = this@SatelliteConfigurationFragment.adaptor
        }
    }
}