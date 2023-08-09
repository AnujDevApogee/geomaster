package com.apogee.geomaster.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ViewPagerAdapter
import com.apogee.geomaster.databinding.HomeScreenMainFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.device.DeviceFragment
import com.apogee.geomaster.ui.projects.ProjectsFragment
import com.apogee.geomaster.ui.survey.SurveyFragment
import com.apogee.geomaster.ui.tools.ToolsFragment
import com.apogee.geomaster.utils.RotateDownPageTransformer
import com.apogee.geomaster.utils.safeNavigate
import com.apogee.geomaster.utils.toastMsg
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.lang.Exception

class HomeScreenMainFragment : Fragment(R.layout.home_screen_main_fragment_layout) {

    private lateinit var binding: HomeScreenMainFragmentLayoutBinding
    private lateinit var viewPagerAdaptor: ViewPagerAdapter

    private val menuItem = arrayOf(
        CbnMenuItem(
            R.drawable.ic_folder,
            R.drawable.anim_folder_vector,
            R.id.projectsFragment
        ),
        CbnMenuItem(
            R.drawable.ic_device,
            R.drawable.avd_ic_device,
            R.id.deviceFragment
        ),
        CbnMenuItem(
            R.drawable.ic_survey,
            R.drawable.avd_anim_survey,
            R.id.surveyFragment
        ),
        CbnMenuItem(
            R.drawable.ic_setting,
            R.drawable.avd_setting,
            R.id.toolsFragment
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeScreenMainFragmentLayoutBinding.bind(view)
        setUpBottomNav()
        setUpAdaptor()
        setUpBottomNavClick()
        Log.i("Current_Page", "onViewCreated: Page ${binding.mainFragmentViewPager.currentItem}")
    }

    private fun setUpBottomNavClick() {
        binding.navView.setOnMenuItemClickListener { _, position ->
            try {
                Log.i("VIEW_PAGER", "onResume: ITEM CLICKED  at $position")
                binding.mainFragmentViewPager.currentItem = position
            } catch (e: Exception) {
                Log.i("VIEW_PAGER", "onResume: $e")
            }
        }
    }

    private fun setUpBottomNav() {
        binding.navView.setMenuItems(menuItem)
    }

    override fun onPause() {
        super.onPause()
        binding.mainFragmentViewPager.currentItem=0
    }

    fun <T> onChildFragmentResponse(response: T) {
        activity?.toastMsg("$response")
        if (response is HomeScreenOption && response.navId != -1) {
            findNavController().safeNavigate(response.navId)
        }
    }

    fun changeToBottom(pos: Int) {
        try {
            binding.navView.onMenuItemClick(pos)
        } catch (e: Exception) {
            binding.navView.onMenuItemClick(0)
        }
    }

    private fun setUpAdaptor() {
        viewPagerAdaptor = ViewPagerAdapter(this)
        viewPagerAdaptor.setFragment(ProjectsFragment())
        viewPagerAdaptor.setFragment(DeviceFragment())
        viewPagerAdaptor.setFragment(SurveyFragment())
        viewPagerAdaptor.setFragment(ToolsFragment())
        binding.mainFragmentViewPager.adapter = viewPagerAdaptor
       binding.mainFragmentViewPager.setPageTransformer(RotateDownPageTransformer())
    }
}