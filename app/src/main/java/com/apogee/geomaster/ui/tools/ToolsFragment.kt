package com.apogee.geomaster.ui.tools

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.ToolsFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.HomeScreenMainFragment
import com.apogee.geomaster.utils.OnItemClickListener


class ToolsFragment : Fragment(R.layout.tools_fragment_layout), OnItemClickListener {

    private lateinit var binding: ToolsFragmentLayoutBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeScreen?)?.showActionBar()
        binding = ToolsFragmentLayoutBinding.bind(view)
        recycleView()
        homeScreenAdaptor.submitList(HomeScreenOption.toolsList)
    }

    private fun recycleView() {
        binding.toolsRecycleView.apply {
            homeScreenAdaptor = HomeScreenAdaptor(this@ToolsFragment)
            adapter = homeScreenAdaptor
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("VIEW_PAGER", "onResume: 3 at Tools Screen")
        (parentFragment as HomeScreenMainFragment?)?.changeToBottom(3)
    }

    override fun <T> onClickListener(response: T) {
        (parentFragment as HomeScreenMainFragment?)?.onChildFragmentResponse(response)
    }
}