package com.apogee.geomaster.ui.projects

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.ProjectsFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.ui.HomeScreenMainFragment
import com.apogee.geomaster.utils.OnItemClickListener

class ProjectsFragment : Fragment(R.layout.projects_fragment_layout), OnItemClickListener {

    private lateinit var binding: ProjectsFragmentLayoutBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeScreen?)?.showActionBar()
        binding = ProjectsFragmentLayoutBinding.bind(view)
        recycleView()
        homeScreenAdaptor.submitList(HomeScreenOption.list)
    }


    private fun recycleView() {
        binding.projectRecycle.apply {
            homeScreenAdaptor = HomeScreenAdaptor(this@ProjectsFragment)
            adapter = homeScreenAdaptor
        }
    }
    override fun onResume() {
        super.onResume()
        Log.i("VIEW_PAGER", "onResume: 0 at Project Screen")
        (parentFragment as HomeScreenMainFragment?)?.changeToBottom(0)
    }
    override fun <T> onClickListener(response: T) {
        (parentFragment as HomeScreenMainFragment?)?.onChildFragmentResponse(response)
    }
}