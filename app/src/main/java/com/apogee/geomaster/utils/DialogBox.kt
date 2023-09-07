package com.apogee.geomaster.utils

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.apogee.geomaster.databinding.MapSettingLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Activity.setUpDialogBox(
    title: String,
    message: String,
    okBtn: String,
    cancel: String? = null,
    icon: Int = -1,
    success: () -> Unit,
    cancelListener: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(
        this, com.google.android.material.R.style.MaterialAlertDialog_Material3
    )

    dialog.setTitle(title)
    dialog.setMessage(message)
    if (icon != -1) {
        dialog.setIcon(icon)
    }
    dialog.setPositiveButton(
        okBtn
    ) { dialogInterface, _ ->
        success.invoke()
        dialogInterface.dismiss()
    }
    if (!cancel.isNullOrEmpty()) {
        dialog.setNegativeButton(
            cancel
        ) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
    }
    dialog.setOnDismissListener {
        cancelListener.invoke()
    }
    dialog.show()
}


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

    binding.ivClose.setOnClickListener {
        dialogBuilder.dismiss()
    }
    dialogBuilder.show()

}