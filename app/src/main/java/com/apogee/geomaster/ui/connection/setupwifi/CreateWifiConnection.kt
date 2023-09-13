package com.apogee.geomaster.ui.connection.setupwifi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.MultiRecyclerViewAdaptor
import com.apogee.geomaster.databinding.CreateWifiConnectionLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.ui.connection.setupconnection.CreateConnectionFragmentArgs
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel
import com.google.android.material.transition.MaterialFadeThrough

class CreateWifiConnection : Fragment(R.layout.create_wifi_connection_layout) {

    private lateinit var binding: CreateWifiConnectionLayoutBinding

    private lateinit var adaptor: MultiRecyclerViewAdaptor

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private val args: CreateConnectionFragmentArgs by navArgs()


    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    private val itemRecycleViewClick=object :OnItemClickListener{
        override fun <T> onClickListener(response: T) {
            Log.i("item_click", "onClickListener: $response")
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
        binding = CreateWifiConnectionLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.wift_setup),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        setUpRecycle()

        getResponse()
        getResponseValue()
    }

    private fun setUpRecycle() {
        binding.recycleView.apply {
            adaptor = MultiRecyclerViewAdaptor(itemRecycleViewClick)
            adapter = adaptor
        }
        adaptor.submitList(DynamicViewType.list)
    }

    private fun getResponse() {
        viewModel.getInputRequiredParma(args.mode, 114)
    }

    private fun getResponseValue() {
        viewModel.dataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("TAG_WIFI", "RADIO_SET_UP ${it.data} and ${it.exception}")
                }

                is ApiResponse.Loading -> {
                    createLog("TAG_WIFI", "RADIO_SET_UP Loading.. ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("TAG_WIFI", "RADIO_SET_UP ${it.data}")
                }
            }
        }
    }

}