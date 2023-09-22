package com.apogee.geomaster.utils

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.apogee.geomaster.databinding.ActivityBasePbLayoutBinding
import kotlin.properties.Delegates


class BaseProgressDialog(private val activity: Activity) {

    private val alertDialog = AlertDialog.Builder(activity).create()

    private lateinit var binding: ActivityBasePbLayoutBinding
    private var totalSize by Delegates.notNull<Double>()
    fun setUI(totalSize: Int) {
        this.totalSize = totalSize.toDouble()
        binding = ActivityBasePbLayoutBinding.inflate(activity.layoutInflater)
        alertDialog.setView(binding.root)
        alertDialog.setTitle("Configuring Base..")
        alertDialog.show()
    }

    fun setProgress(currentSize: Double) {
        val currentItem = totalSize - currentSize
        Log.i("TAG_PROGRESS", "setProgress: $currentItem/$totalSize and ${((currentItem/totalSize)*100)}")
        binding.count.text = "${currentItem.toInt()}/${totalSize.toInt()}"
        binding.pbLayout.progress = ((currentItem / totalSize) * 100).toInt()
    }

    fun hideDialog() {
        totalSize=0.toDouble()
        alertDialog.dismiss()
    }
}