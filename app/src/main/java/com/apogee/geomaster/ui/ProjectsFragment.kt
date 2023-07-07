package com.apogee.geomaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ProjectsFragmentLayoutBinding

class ProjectsFragment : Fragment(R.layout.projects_fragment_layout) {
    private lateinit var binding: ProjectsFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectsFragmentLayoutBinding.bind(view)

    }
}