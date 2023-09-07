package com.apogee.geomaster.ui.configuration.miscellaneous


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.ViewModel.DatabaseViewModel
import com.apogee.geomaster.databinding.MiscellaneousLayoutBinding
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.showMessage

class MiscellaneousFragment : Fragment(R.layout.miscellaneous_layout) {

    private lateinit var binding: MiscellaneousLayoutBinding
    private val args by navArgs<MiscellaneousFragmentArgs>()
    private val viewModel: DatabaseViewModel by viewModels()

    private val TAG = "MiscellaneousFragment"
    var pointNameValue = "N"
    var codeName = "N"
    var LRF = "N"
    var OSM = "N"
    var satellite = "N"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MiscellaneousLayoutBinding.bind(view)
        displayActionBar("Miscellaneous Configuration", binding.actionLayout)
        Log.d(
            TAG,
            "onViewCreated:Miscellaneous  args.item---${args.surveyConfigName},${args.satelliteConfigName}"
        )
        CheckListner()
        getDBresponseListner()

        binding.doneBtn.setOnClickListener {
            Log.d(TAG,"CheckListner: pointName--$pointNameValue\n --codeName--$codeName \n--LRF--$LRF\n--OSM--$OSM\n--satellite--$satellite")
            viewModel.insertMiscellaneousConfigData("${args.surveyConfigName}SatConfig,$pointNameValue,$LRF,$codeName,$OSM,$satellite")
        }
    }

    private fun getDBresponseListner() {
        viewModel.dbResponse.observe(viewLifecycleOwner) { res ->
            res?.let {
                if (it is Pair<*, *>) {
                    showMessage(it.first as String)
                    if (it.second as Boolean) {
                        findNavController().safeNavigate(
                            MiscellaneousFragmentDirections.actionMiscellaneousFragmentToDeviceConfiguration(
                                args.surveyConfigName,
                                args.satelliteConfigName
                            )
                        )
                    }
                }

            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.init()
    }

    fun CheckListner() {

        binding.pointNameVisible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pointNameValue = "Y"
            } else {
                pointNameValue = "N"
            }
        }

        binding.lfrSwitchCase.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LRF = "Y"
            } else {
                LRF = "N"
            }

        }

        binding.codeNameSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                codeName = "Y"
            } else {
                codeName = "N"
            }
        }

        binding.osmViewSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.satelliteView.isChecked = false
                if (isChecked) {
                    OSM = "Y"
                } else {
                    OSM = "N"
                }
                satellite = "N"
            } else {
                OSM = "N"
            }
        }
        binding.satelliteView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.osmViewSwitch.isChecked = false
                if (isChecked) {
                    satellite = "Y"
                } else {
                    satellite = "N"
                }

                OSM = "N"
            } else {
                satellite = "N"
            }
        }
    }

}


