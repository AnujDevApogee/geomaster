package com.apogee.geomaster.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.apogee.geomaster.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_progress_dialog)
        setCancelable(false)
    }

    fun setMessage(message: String) {
        val textView = findViewById<TextView>(R.id.customProgressMessage)
        textView.text = message
    }

    fun setProgress(progress: Int) {
        val progressBar = findViewById<ProgressBar>(R.id.customProgressBar)
        progressBar.progress = progress
    }
}