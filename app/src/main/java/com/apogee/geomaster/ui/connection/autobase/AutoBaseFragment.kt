package com.apogee.geomaster.ui.connection.autobase

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.MultiRecyclerViewAdaptor
import com.apogee.geomaster.databinding.AutoBaseLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.connection.mannualbase.ManualBaseFragmentArgs
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel

class AutoBaseFragment : Fragment(R.layout.auto_base_layout) {

    private lateinit var binding: AutoBaseLayoutBinding

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private lateinit var adaptor: MultiRecyclerViewAdaptor

    private val args: AutoBaseFragmentArgs by navArgs()



    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    private val itemRecycleViewClick = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            Log.i("item_click", "onClickListener: $response")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AutoBaseLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.setup_auto_bs),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()

        setUpRecycle()
        getResponse()
        getResponseValue()

    }


    private fun setUpRecycle() {
        binding.recycleViewInternet.apply {
            adaptor = MultiRecyclerViewAdaptor(itemRecycleViewClick)
            adapter = adaptor
        }
    }

    private fun getResponse() {
        viewModel.getInputRequiredParma(args.mode, 114)
    }

    private fun getResponseValue() {
        viewModel.dataResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Error -> {
                    createLog("TAG_AUTO", "AUTO_SET_UP ${it.data} and ${it.exception}")
                }

                is ApiResponse.Loading -> {
                    createLog("TAG_AUTO", "AUTO_SET_UP Loading.. ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("TAG_AUTO", "AUTO_SET_UP ${it.data}")
                    val list = it.data as List<DynamicViewType>
                    adaptor.submitList(list)
                }
            }
        }
    }



}