package com.apogee.geomaster.ui.projects.projectlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ProjectItemFragmentBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar

class ProjectListFragment : Fragment(R.layout.project_item_fragment) {

    private lateinit var binding: ProjectItemFragmentBinding

    private val menuCallback=object :OnItemClickListener{
        override fun <T> onClickListener(response: T) {

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= ProjectItemFragmentBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()
        displayActionBar("Projects",binding.actionLayout,R.menu.info_mnu,menuCallback)

    }


}