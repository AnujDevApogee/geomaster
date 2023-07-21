package com.apogee.geomaster.ui.projects.createproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.DefaultProjectLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.getEmojiByUnicode
import com.apogee.geomaster.utils.openKeyBoard
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.setHtmlTxt

class DefaultCreateProjectFragment : Fragment(R.layout.default_project_layout) {

    private lateinit var binding: DefaultProjectLayoutBinding

    private val menuCallback = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DefaultProjectLayoutBinding.bind(view)
        displayActionBar(
            "Create Project ${getEmojiByUnicode(0x1F4DD)}",
            binding.actionLayout,
            R.menu.info_mnu,
            menuCallback
        )
        (activity as HomeScreen?)?.hideActionBar()
        activity?.openKeyBoard(binding.projectNme)
        binding.projectDetailInfo.apply {
            text= setHtmlBoldTxt("Dataum\t\t")
            append(setHtmlTxt("WGS84","'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Projection\t\t"))
            append(setHtmlTxt("UTM","'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Distance\t\t"))
            append(setHtmlTxt("meter","'#0E4A88'"))
            append("\n")
            append(setHtmlBoldTxt("Angle\t\t"))
            append(setHtmlTxt("DD","'#0E4A88'"))
            append("\n")

        }
        binding.createNewProject.setOnClickListener {
            findNavController().navigate(DefaultCreateProjectFragmentDirections.actionDefaultCreateProjectFragmentToCreateProjectFragment())
        }
    }

}