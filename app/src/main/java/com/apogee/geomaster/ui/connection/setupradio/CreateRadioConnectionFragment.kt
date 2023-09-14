package com.apogee.geomaster.ui.connection.setupradio

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.MultiRecyclerViewAdaptor
import com.apogee.geomaster.databinding.CreateRadioConnLayoutFragmentBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel
import com.google.android.material.transition.MaterialFadeThrough

@Suppress("UNCHECKED_CAST")
class CreateRadioConnectionFragment : Fragment(R.layout.create_radio_conn_layout_fragment) {

    private lateinit var binding: CreateRadioConnLayoutFragmentBinding

    private val args: CreateRadioConnectionFragmentArgs by navArgs()

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private lateinit var adaptor: MultiRecyclerViewAdaptor

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    private val mapList = mutableMapOf<String, Any?>()

    private val itemRecycleViewClick = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            Log.i("item_click", "onClickListener: $response")
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
        binding = CreateRadioConnLayoutFragmentBinding.bind(view)
        displayActionBar(
            getString(R.string.radio_comm), binding.actionLayout, R.menu.info_mnu, menuCallback
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

        }

    }

    private fun setUpRecycle() {
        binding.recycleViewRadio.apply {
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
                    createLog("TAG_RADIO", "RADIO_SET_UP ${it.data} and ${it.exception}")
                }

                is ApiResponse.Loading -> {
                    createLog("TAG_RADIO", "RADIO_SET_UP Loading.. ${it.data}")
                }

                is ApiResponse.Success -> {
                    createLog("TAG_RADIO", "RADIO_SET_UP ${it.data}")
                    val list=it.data as List<DynamicViewType>
                    adaptor.submitList(list)
                }
            }
        }
    }


}