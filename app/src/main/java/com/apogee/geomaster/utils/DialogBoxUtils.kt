package com.apogee.geomaster.utils

import androidx.fragment.app.Fragment
import com.apogee.geomaster.databinding.SetupDeviceLayoutBinding
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Fragment.showDeviceAdd(
    success: (String) -> Unit,
    cancel: () -> Unit
) {
    val materialDialogs = MaterialAlertDialogBuilder(
        requireActivity(), R.style.MaterialAlertDialog_Material3
    )
    val binding = SetupDeviceLayoutBinding.inflate(layoutInflater)

    materialDialogs.setCancelable(false)
    materialDialogs.setView(binding.root)
    val alertDialog = materialDialogs.show()

    binding.btnCancel.setOnClickListener {
        cancel.invoke()
        activity?.closeKeyboard(binding.serialEd)
        alertDialog.dismiss()
    }
    activity?.openKeyBoard(binding.serialEd)

    binding.btnDone.setOnClickListener {
        val text = binding.serialEd.text.toString()
        if (isInvalidString(text)) {
            showMessage("Please Add Correct Device Info")
        } else {
            success.invoke(text)
            activity?.closeKeyboard(binding.serialEd)
            alertDialog.dismiss()
        }
    }

}
