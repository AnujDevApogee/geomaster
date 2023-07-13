package com.apogee.geomaster.ui.projects.projectlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ProjectListAdaptor
import com.apogee.geomaster.databinding.ProjectItemFragmentBinding
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar

class ProjectListFragment : Fragment(R.layout.project_item_fragment) {

    private lateinit var binding: ProjectItemFragmentBinding

    private lateinit var projectListAdaptor: ProjectListAdaptor

    private val recycleAdaptorCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProjectItemFragmentBinding.bind(view)
        displayActionBar("Projects", binding.actionLayout, R.menu.info_mnu, menuCallback)
        (activity as HomeScreen?)?.hideActionBar()
        setUpRecycleView()
        projectListAdaptor.submitList(Project.list)
    }

    private fun setUpRecycleView() {
        binding.recycleViewProject.apply {
            projectListAdaptor= ProjectListAdaptor(recycleAdaptorCallback)
            adapter =projectListAdaptor
        }
    }


}