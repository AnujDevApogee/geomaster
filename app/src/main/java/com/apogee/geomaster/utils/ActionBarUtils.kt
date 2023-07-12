package com.apogee.geomaster.utils

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActionBarLayoutBinding


fun Fragment.displayActionBar(
    srcTitle: String,
    binding: ActionBarLayoutBinding,
    menu: Int = -1,
    onMenuItem: OnItemClickListener? = null
) {
    binding.toolBar.setNavigationIcon(R.drawable.ic_back_arrow)
    if (menu != -1) {
        binding.toolBar.inflateMenu(menu)
        binding.toolBar.setOnMenuItemClickListener {
            onMenuItem?.onClickListener(it)
            return@setOnMenuItemClickListener true
        }
    }
    binding.mainScreenLayout.hide()
    binding.scrName.show()
    binding.scrName.text = srcTitle

    binding.toolBar.setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}


fun Activity.displayActionBar(
    satellite: String,
    battery: String,
    bluetooth: String,
    device: String,
    mnu: Int,
    binding: ActionBarLayoutBinding,
    itemClickListener: OnItemClickListener
) {

    binding.toolBar.inflateMenu(mnu)
    binding.toolBar.setOnMenuItemClickListener {
        itemClickListener.onClickListener(it)
        return@setOnMenuItemClickListener true
    }

    binding.satelliteIcon.setOnClickListener(itemClickListener)
    binding.satelliteTxtTv.text = satellite

    binding.deviceIcon.setOnClickListener(itemClickListener)
    binding.deviceTxtTv.text = battery

    binding.blueToothIcon.setOnClickListener(itemClickListener)
    binding.blueToothTxtTv.text = bluetooth

    binding.batteryIcon.setOnClickListener(itemClickListener)
    binding.batteryTxtTv.text = device


}

private fun View.setOnClickListener(itemClickListener: OnItemClickListener) {
    setOnClickListener {
        itemClickListener.onClickListener(id)
    }
}