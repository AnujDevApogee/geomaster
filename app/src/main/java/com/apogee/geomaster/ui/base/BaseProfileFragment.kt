@file:Suppress("UNCHECKED_CAST")

package com.apogee.geomaster.ui.base

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.BaseProfileLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.connection.antenna.SetUpAntennaFragment
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getColorInt
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.toastMsg
import com.apogee.geomaster.viewmodel.BaseConfigurationViewModel
import com.google.android.material.transition.MaterialFadeThrough

class BaseProfileFragment : Fragment(R.layout.base_profile_layout) {
    private lateinit var binding: BaseProfileLayoutBinding

    private val viewModel: BaseConfigurationViewModel by viewModels()

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }
    private var list= mutableListOf<String>()

    companion object {
        const val DeviceName = "NAVIK200-1.0"
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
        binding = BaseProfileLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.gnss_base_profile),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        initial()

        getSetConfigResponses()

        binding.setConnBtn.setOnClickListener {
            activity?.toastMsg("Connection click")
        }


        /*binding.deviceInfo.append(setHtmlBoldTxt("Make"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")
        binding.deviceInfo.append(setHtmlBoldTxt("Model"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")
        binding.deviceInfo.append(setHtmlBoldTxt("Device Name"))
        binding.deviceInfo.append("\t")
        binding.deviceInfo.append("xxxxxxxx")
        binding.deviceInfo.append("\n")*/




        if (SetUpAntennaFragment.measuredHeight!=-1){
            binding.setAntennaBtn.hide()
            binding.antennaCard.show()
            binding.antennaType.text="Antenna ${SetUpAntennaFragment.measuredHeight}m"
            binding.setConnBtn.isCheckable=true
            binding.setConnBtn.isEnabled=true
            binding.setConnBtn.backgroundTintList= ColorStateList.valueOf(requireActivity().getColorInt(R.color.md_theme_dark_inversePrimary))
        }





        binding.setAntennaBtn.setOnClickListener {
            findNavController().safeNavigate(BaseProfileFragmentDirections.actionBaseProfileFragmentToSetUpAntennaFragment())
        }


        binding.setConnBtn.setOnClickListener {
            findNavController().safeNavigate(BaseProfileFragmentDirections.actionGlobalConnectionFragment(list.toTypedArray()))
        }
    }

    private fun initial() {
        viewModel.setUpConfig(DeviceName)
    }

    private fun getSetConfigResponses() {
        viewModel.baseConfigDataSetUp.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("BASE_SETUP", "Exception => ${it.exception?.message}")
                }

                is ApiResponse.Loading -> {
                    createLog("BASE_SETUP", "Loading ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("BASE_SETUP", "Success ${it.data}")
                    val response = it.data as Pair<*, *>
                    binding.deviceInfo.apply {
                        text = ""
                        append(setHtmlBoldTxt("Model"))
                        append("\t")
                        append(DeviceName)
                        append("\n")
                        append(setHtmlBoldTxt("Device Name"))
                        append("\t")
                        append(DeviceName)
                        // Renaming Info
                        append("\n")
                        append(setHtmlBoldTxt("Make"))
                        append("\t ")
                        append(response.first as String)
                        list.clear()
                        list.addAll(response.second as List<String>)
                    }
                }
            }
        }
    }

}