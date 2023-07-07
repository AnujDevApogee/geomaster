package com.apogee.geomaster.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.HomeScreenLayoutBinding
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import java.lang.Exception

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: HomeScreenLayoutBinding
    private lateinit var navHostFragment: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val navHost =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navHostFragment = navHost.findNavController()
            setUpBottomNav()
        } catch (e: Exception) {
            Log.i("Exception_ICON", "onCreate: ${e.message}")
        }
    }


    private fun setUpBottomNav() {
        val menuItem = arrayOf(
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
        binding.navView.setMenuItems(menuItem)
        binding.navView.setupWithNavController(navHostFragment)
    }


}