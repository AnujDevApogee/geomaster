package com.apogee.geomaster.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.BaseProfileLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.google.android.material.transition.MaterialFadeThrough

class BaseProfileFragment : Fragment(R.layout.base_profile_layout) {
    private lateinit var binding: BaseProfileLayoutBinding
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

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
        binding = BaseProfileLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.gnss_base_profile),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        binding.deviceInfo.append(setHtmlBoldTxt("Make"))
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
        binding.deviceInfo.append("\n")
        binding.setConnBtn.setOnClickListener {
            findNavController().safeNavigate(BaseProfileFragmentDirections.actionGlobalConnectionFragment())
        }
    }

}