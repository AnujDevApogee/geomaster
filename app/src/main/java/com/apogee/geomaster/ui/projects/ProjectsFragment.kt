package com.apogee.geomaster.ui.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.ProjectsFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.utils.OnItemClickListener

class ProjectsFragment : Fragment(R.layout.projects_fragment_layout), OnItemClickListener {
    private lateinit var binding: ProjectsFragmentLayoutBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun <T> onClickListener(response: T) {
        findNavController().navigate(R.id.action_projectsFragment_to_testingFragment)
    }
}