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
import com.apogee.geomaster.utils.MyPreference
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.setUpDialogBox
import com.google.android.material.transition.MaterialFadeThrough

class ProjectListFragment : Fragment(R.layout.project_item_fragment) {

    private lateinit var binding: ProjectItemFragmentBinding
    var projectListData : ArrayList<String> = ArrayList()
    var projectListDataCustomProjection : ArrayList<String> = ArrayList()

    private lateinit var myPreference :MyPreference
    
    val TAG= "ProjectListFragment"

    private lateinit var projectListAdaptor: ProjectListAdaptor
    private lateinit var dbControl: DatabaseRepsoitory


    private val recycleAdaptorCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            Log.i(TAG, "onClickListener: itemclick_project")
            if (response is Project){
                activity?.setUpDialogBox("Information","Continue with ${response.title}","Continue","Cancel", success = {
                    myPreference.putStringData("Last_Used",response.title)
                    Log.i(TAG, "onClickListener: LastUsed_saved -> ${myPreference.getStringData("Last_Used")}")
                    findNavController().navigate(R.id.action_projectListFragment_to_homeScreenMainFragment)
                }, cancelListener = {

                })

            }
        }
    }
    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            findNavController().navigate(R.id.action_projectListFragment_to_configurationFragment)
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
        myPreference=MyPreference.getInstance(requireContext())
        dbControl = DatabaseRepsoitory(this.requireContext())

        displayActionBar("Projects ${getEmojiByUnicode( 0x1F4C1)}", binding.actionLayout, R.menu.info_mnu, menuCallback)
        (activity as HomeScreen?)?.hideActionBar()
        setUpRecycleView()
        projectListData= dbControl.getProjectList() as ArrayList<String>


        
        val value= myPreference.getStringData("Last_Used")

//        projectListData= dbControl.getProjectList() as ArrayList<String>
//        projectListDataCustomProjection= dbControl.getProjectListCustomProjection() as ArrayList<String>
        var projectDetails : ArrayList<Project> = ArrayList()
//        projectDetails.add(Project("Default","WGS84","44"))
        for(i in projectListData){
            var  title=i.split(",")[0]
            var  configurationName= i.split(",")[1]
     /*       var  zone="44"
            var  projectionType="UTM"*/
            projectDetails.add(Project(title,configurationName))
        }

        
        val obj=projectDetails.find {
            it.title==value
        }
        val ls= mutableListOf<Project>()
        obj?.let {
            ls.add(obj)
            projectDetails.remove(obj)
        }
        ls.addAll(projectDetails)
        projectListAdaptor.submitList(ls)

  /*      for(i in projectListDataCustomProjection){
            var  title=i.split(",")[0]
            var  datum=i.split(",")[1]
            var  zone=i.split(",")[2]
            var  projectionType=i.split(",")[3]

            projectDetails.add(Project(title,datum,projectionType,zone))
        }*/
        //projectListAdaptor.submitList(projectDetails)

        Log.d(TAG, "onViewCreated: projectListData $projectListData")

        binding.addProject.setOnClickListener {
            findNavController().navigate(R.id.action_projectListFragment_to_configurationFragment)
        }
    }

    private fun setUpRecycleView() {
        binding.recycleViewProject.apply {
            projectListAdaptor = ProjectListAdaptor(recycleAdaptorCallback)
            adapter = projectListAdaptor
        }
    }


}