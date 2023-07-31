package com.apogee.geomaster.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.HomeScreenLayoutBinding
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.changeStatusBarColor
import com.apogee.geomaster.utils.displayActionBar
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.toastMsg


class HomeScreen : AppCompatActivity() {

    private lateinit var binding: HomeScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenLayoutBinding.inflate(layoutInflater)
        changeStatusBarColor(R.color.md_theme_light_primary)
        displayActionBar(
            "12",
            "23%",
            "Test_001",
            "Present",
            R.menu.info_mnu,
            binding.actionLayout,
            object :
                OnItemClickListener {
                override fun <T> onClickListener(response: T) {
                    if (response is Int) {
                        mainActionClick(response as Int)
                    }
                    if (response is MenuItem) {
                        toastMsg(response.title.toString())
                    }
                }
            })
        setContentView(binding.root)
    }

    private fun mainActionClick(response: Int) {
        when (response) {
            R.id.satellite_icon -> {
                toastMsg("Satellite Connection")
            }

            R.id.battery_icon -> {
                toastMsg("Bluetooth Icon")
            }

            R.id.blue_tooth_icon -> {

            }

            R.id.device_icon -> {

            }
        }
    }




    fun showActionBar() {
        binding.actionLayout.root.show()
    }


    fun hideActionBar(){
        binding.actionLayout.root.hide()
    }

}