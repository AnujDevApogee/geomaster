package com.example.stakemodual.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.stakemodual.databinding.MapSettingLayoutBinding
import com.example.stakemodual.utils.MapType


fun Activity.setUpDialogInfo(mapListen: (String) -> Unit) {
    val dialogBuilder = AlertDialog.Builder(this).create()
    val binding = MapSettingLayoutBinding.inflate(layoutInflater)
    dialogBuilder.setView(binding.root)

    binding.ibmap.setOnClickListener {
        mapListen.invoke(MapType.PLANEVIEW.name)
        dialogBuilder.dismiss()
    }

    binding.satview.setOnClickListener {
        mapListen.invoke(MapType.STATALLITE.name)
        dialogBuilder.dismiss()
    }

    binding.ivImage.setOnClickListener {
        mapListen.invoke(MapType.STEETVIEW.name)
        dialogBuilder.dismiss()
    }

    dialogBuilder.show()

}