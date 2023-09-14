package com.apogee.geomaster.ui.connection.setupconnection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.MultiRecyclerViewAdaptor
import com.apogee.geomaster.databinding.CreateConnectionLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.ui.connection.internet.InternetFragment
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel
import com.google.android.material.transition.MaterialFadeThrough

@Suppress("UNCHECKED_CAST")
class CreateConnectionFragment : Fragment(R.layout.create_connection_layout) {

    private lateinit var binding: CreateConnectionLayoutBinding

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private val args: CreateConnectionFragmentArgs by navArgs()

    private lateinit var adaptor: MultiRecyclerViewAdaptor

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    private val mapList = mutableMapOf<String, Any?>()

    private val itemRecycleViewClick = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            if (response is DynamicViewType) {
                when (response) {
                    is DynamicViewType.EditText -> {
                        if (response.data.isNullOrEmpty() && mapList.containsKey(response.hint)) {
                            mapList.remove(response.hint)
                        } else {
                            mapList[response.hint] = response.data
                        }
                    }

                    is DynamicViewType.SpinnerData -> {
                        if (response.selectedPair != null) {
                            mapList[response.hint] = response.selectedPair
                        }
                    }
                }
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
        binding = CreateConnectionLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.new_corr_source),
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        setUpRecycle()
        getResponse()
        getResponseValue()
        binding.doneBtn.setOnClickListener {
            if (adaptor.itemCount != mapList.size) {
                showMessage("Please Add All the configuration")
                return@setOnClickListener
            }
            createLog("TAG_RESPONSE","Done Part is Successfully $mapList")
            InternetFragment.internetWifi.add(mapList)
            findNavController().popBackStack()
        }
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
                    createLog("TAG_Internet", "RADIO_SET_UP ${it.data} and ${it.exception}")
                }

                is ApiResponse.Loading -> {
                    createLog("TAG_Internet", "RADIO_SET_UP Loading.. ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("TAG_Internet", "RADIO_SET_UP ${it.data}")
                    val list = it.data as List<DynamicViewType>
                    adaptor.submitList(list)
                }
            }
        }
    }

}