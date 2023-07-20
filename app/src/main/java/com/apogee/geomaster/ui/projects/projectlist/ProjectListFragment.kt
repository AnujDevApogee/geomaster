package com.apogee.geomaster.ui.projects.projectlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ProjectListAdaptor
import com.apogee.geomaster.databinding.ProjectItemFragmentBinding
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.safeNavigate
import com.google.android.material.transition.MaterialFadeThrough

class ProjectListFragment : Fragment(R.layout.project_item_fragment) {

    private lateinit var binding: ProjectItemFragmentBinding
    var projectListData : ArrayList<String> = ArrayList()

    val TAG= "ProjectListFragment"

    private lateinit var projectListAdaptor: ProjectListAdaptor
    private lateinit var dbControl: DatabaseRepsoitory


    private val recycleAdaptorCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

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
        binding = ProjectItemFragmentBinding.bind(view)
        dbControl = DatabaseRepsoitory(this.requireContext())

        displayActionBar("Projects ${getEmojiByUnicode( 0x1F4C1)}", binding.actionLayout, R.menu.info_mnu, menuCallback)
        (activity as HomeScreen?)?.hideActionBar()
        setUpRecycleView()

        projectListData= dbControl.getProjectList() as ArrayList<String>
        var projectDetails : ArrayList<Project> = ArrayList()

        for(i in projectListData){
            var  title=i.split(",")[0]
            var  datum=i.split(",")[1]
            var  zone=i.split(",")[2]
            projectDetails.add(Project(title,datum,zone))
        }
        projectListAdaptor.submitList(projectDetails)

        Log.d(TAG, "onViewCreated: projectListData $projectListData")

        binding.addProject.setOnClickListener {
            findNavController().safeNavigate(ProjectListFragmentDirections.actionProjectListFragmentToCreateProjectFragment())
        }
    }

    private fun setUpRecycleView() {
        binding.recycleViewProject.apply {
            projectListAdaptor = ProjectListAdaptor(recycleAdaptorCallback)
            adapter = projectListAdaptor
        }
    }


}