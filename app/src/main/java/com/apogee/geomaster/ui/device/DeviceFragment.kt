package com.apogee.geomaster.ui.device

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.DeviceLayoutFragmentBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.toastMsg


class DeviceFragment : Fragment(R.layout.device_layout_fragment), OnItemClickListener {

    private lateinit var binding: DeviceLayoutFragmentBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as  HomeScreen?)?.showActionBar()
        binding = DeviceLayoutFragmentBinding.bind(view)
        recycleView()
        homeScreenAdaptor.submitList(HomeScreenOption.list)
    }

    private fun recycleView() {
        binding.deviceRecycleView.apply {
            homeScreenAdaptor = HomeScreenAdaptor(this@DeviceFragment)
            adapter = homeScreenAdaptor
        }
    }

    override fun <T> onClickListener(response: T) {
        activity?.toastMsg("$response")
    }
}