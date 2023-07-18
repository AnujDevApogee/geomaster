package com.apogee.geomaster.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityStartBinding
import com.apogee.geomaster.service.ApiService
import com.apogee.geomaster.service.Constants
import com.apogee.geomaster.ui.login.LoginActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class StartActivity : AppCompatActivity() {
    var TAG: String = StartActivity::class.java.simpleName
    var binding: ActivityStartBinding? = null
    var sharedPreferences: SharedPreferences? = null
    var responseString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        RequestMultiplePermission()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@StartActivity)




        /*Saving package name for check first time installation*/
        responseString = sharedPreferences!!.getString(Constants.RESPONSE_STRING, null)
        Log.d(TAG, "onCreate:responseString $responseString")
        /*Button click event*/
        if (responseString.equals("")) {
            binding!!.btnLetstart.isClickable = false
            binding!!.btnLetstart.isFocusable = false
            binding!!.btnLetstart.isEnabled = false
            binding!!.view.visibility = View.VISIBLE
        } else {
            binding!!.btnLetstart.isClickable = true
            binding!!.btnLetstart.isEnabled = true
            binding!!.view.visibility = View.GONE
        }

        binding?.btnLetstart?.setOnClickListener {
            if (responseString != null) {
                Log.d(TAG, "onCreate:APISERVICE StartElse")
                val intents = Intent(this@StartActivity, LoginActivity::class.java)
                startActivity(intents)
                finish()
            } else {
                Log.d(TAG, "onCreate:APISERVICE StartElse")
                AlertDialog.Builder(this@StartActivity)
                    .setTitle("Message")
                    .setMessage("Database is not created properly. Please try again") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        android.R.string.yes,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                val intentService =Intent(this@StartActivity, ApiService::class.java)
                                startService(intentService)
                                dialog!!.dismiss()
                            }
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }

            binding!!.btnLetstart.isClickable = false

        }
    }

    /*Check first run in onResume*/
    override fun onResume() {
        super.onResume()
        binding!!.btnLetstart.isClickable = true
        if (responseString == null) {
            val intentService = Intent(this@StartActivity, ApiService::class.java)
            startService(intentService)
            responseString = sharedPreferences!!.getString(Constants.RESPONSE_STRING, "").toString()
        }
    }

    /*CheckNetworkConnection*/


    //Permission function starts from here
    private fun RequestMultiplePermission() {

        Dexter.withContext(this@StartActivity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                    }// check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }


    fun updateUI() {
        this@StartActivity.runOnUiThread {

            try {
                binding!!.btnLetstart.isClickable = true
                binding!!.btnLetstart.isFocusable = true
                binding!!.btnLetstart.isEnabled = true
                binding!!.view.visibility = View.GONE
            } catch (e: Exception) {

            }

        }
    }


}