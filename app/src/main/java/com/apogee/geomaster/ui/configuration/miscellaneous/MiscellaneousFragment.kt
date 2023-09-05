package com.apogee.geomaster.ui.configuration.miscellaneous


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.MiscellaneousLayoutBinding
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate

class MiscellaneousFragment : Fragment(R.layout.miscellaneous_layout) {

    private lateinit var binding: MiscellaneousLayoutBinding
    private val args by navArgs<MiscellaneousFragmentArgs>()
    private val TAG = "MiscellaneousFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MiscellaneousLayoutBinding.bind(view)
        displayActionBar("Miscellaneous Configuration", binding.actionLayout)
        Log.d(TAG, "onViewCreated:Miscellaneous  args.item---${args.surveyConfigName},${args.satelliteConfigName}")
        binding.doneBtn.setOnClickListener {
            CheckListner()
//            findNavController().safeNavigate(MiscellaneousFragmentDirections.actionMiscellaneousFragmentToDeviceConfiguration(args.surveyConfigName,args.satelliteConfigName))
        }

    }
    fun CheckListner(){

var pointName = ""
        binding.pointNameVisible.setOnCheckedChangeListener{ _, isChecked ->
             pointName = if (isChecked) "Switch1:ON" else "Switch1:OFF"
            Log.d(TAG, "CheckListner: pointName--$pointName")

        }

    }

}
