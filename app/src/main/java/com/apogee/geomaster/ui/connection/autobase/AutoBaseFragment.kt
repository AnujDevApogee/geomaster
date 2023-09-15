package com.apogee.geomaster.ui.connection.autobase

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.MultiRecyclerViewAdaptor
import com.apogee.geomaster.databinding.AutoBaseLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.base.BaseProfileFragment
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.viewmodel.SetUpConnectionViewModel

class AutoBaseFragment : Fragment(R.layout.auto_base_layout) {

    private lateinit var binding: AutoBaseLayoutBinding

    private val viewModel: SetUpConnectionViewModel by viewModels()

    private lateinit var adaptor: MultiRecyclerViewAdaptor

    private val args: AutoBaseFragmentArgs by navArgs()
    private val sharePreference by lazy {
        MyPreference.getInstance(requireActivity())
    }


    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }


        private var baseSetUp = mutableMapOf<String, Any?>()


    private val itemRecycleViewClick = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            if (response is DynamicViewType) {
                when (response) {
                    is DynamicViewType.EditText -> {
                        if (response.data.isNullOrEmpty() && baseSetUp.containsKey(response.hint)) {
                            baseSetUp.remove(response.hint)
                        } else {
                            baseSetUp[response.hint] = response.data
                        }
                    }

                    is DynamicViewType.SpinnerData -> {
                        if (response.selectedPair != null) {
                            baseSetUp[response.hint] = response.selectedPair
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AutoBaseLayoutBinding.bind(view)
        displayActionBar(
            getString(R.string.setup_auto_bs), binding.actionLayout, R.menu.info_mnu, menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()

        setUpRecycle()
        getResponse()
        getResponseValue()
        binding.doneBtn.setOnClickListener {
            if (adaptor.itemCount != baseSetUp.size) {
                showMessage("Please Add all the information")
                return@setOnClickListener
            }
            BaseProfileFragment.baseSetUp= Pair("Auto base",baseSetUp.toMap())
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
        viewModel.getInputRequiredParma(args.mode, sharePreference.getStringData(Constants.DGPS_DEVICE_ID).toInt())
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