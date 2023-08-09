package com.apogee.geomaster.ui.device

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.DeviceLayoutFragmentBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.HomeScreenMainFragment
import com.apogee.geomaster.utils.OnItemClickListener


class DeviceFragment : Fragment(R.layout.device_layout_fragment), OnItemClickListener {

    private lateinit var binding: DeviceLayoutFragmentBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as  HomeScreen?)?.showActionBar()
        binding = DeviceLayoutFragmentBinding.bind(view)
        recycleView()
        homeScreenAdaptor.submitList(HomeScreenOption.deviceList)
    }

    private fun recycleView() {
        binding.deviceRecycleView.apply {
            homeScreenAdaptor = HomeScreenAdaptor(this@DeviceFragment)
            adapter = homeScreenAdaptor
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("VIEW_PAGER", "onResume: 1 at Device Screen")
        (parentFragment as HomeScreenMainFragment?)?.changeToBottom(1)
    }

    override fun <T> onClickListener(response: T) {
        (parentFragment as HomeScreenMainFragment?)?.onChildFragmentResponse(response)
    }
}