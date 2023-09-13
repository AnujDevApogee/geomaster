package com.apogee.geomaster.ui.connection.setupconnection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.CreateConnectionLayoutBinding
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel
import com.google.android.material.transition.MaterialFadeThrough

class CreateConnectionFragment : Fragment(R.layout.create_connection_layout) {

    private lateinit var binding: CreateConnectionLayoutBinding

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private val args: CreateConnectionFragmentArgs by navArgs()

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
        binding = CreateConnectionLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.new_corr_source),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )

        getResponse()
        getResponseValue()
    }

    private fun getResponse() {
        viewModel.getInputRequiredParma(args.mode, 114)
    }

    private fun getResponseValue() {
        viewModel.dataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("TAG_Internet", "RADIO_SET_UP ${it.data} and ${it.exception}")
                }

                is ApiResponse.Loading -> {
                    createLog("TAG_Internet", "RADIO_SET_UP Loading.. ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("TAG_Internet", "RADIO_SET_UP ${it.data}")
                }
            }
        }
    }

}