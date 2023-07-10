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

class StartActivity : AppCompatActivity() {
    var TAG: String = StartActivity::class.java.simpleName
    var binding: ActivityStartBinding? = null
    private var prefs: SharedPreferences? = null
    private lateinit var receiver: serverResponseReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        RequestMultiplePermission()
        CheckingPermissionIsEnabledOrNot()

        receiver = serverResponseReceiver()

        /*SAving package name for check first time installation*/
        prefs = getSharedPreferences("com.apogee.geomaster", MODE_PRIVATE)
        /*Button click event*/
        if (prefs!!.getBoolean("firstrun", true)) {
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
            Log.d(TAG, "onCreate: " + prefs!!.getBoolean("database_created", false))
            if (prefs!!.getBoolean("database_created", true)) {

                val intents = Intent(this@StartActivity, LoginActivity::class.java)
                startActivity(intents)
                finish()
            } else {
                AlertDialog.Builder(this@StartActivity)
                    .setTitle("Message")
                    .setMessage("Database is not created properly. Please try again") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(
                        android.R.string.yes,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                val intentService =
                                    Intent(this@StartActivity, ApiService::class.java)
                                startService(intentService)
                                receiver = serverResponseReceiver()
                                dialog!!.dismiss()
                            }
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }

            binding!!.btnLetstart.isClickable = false

        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, IntentFilter("GET_SERVER_DATA"), RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, IntentFilter("GET_SERVER_DATA"))
        }


    }

    /*Check first run in onResume*/
    override fun onResume() {
        super.onResume()
        binding!!.btnLetstart.isClickable = true
        isNetworkConnectionAvailable
        if (prefs!!.getBoolean("firstrun", true)) {
            val intentService = Intent(this@StartActivity, ApiService::class.java)
            startService(intentService)
            prefs!!.edit().putBoolean("firstrun", false).apply()
        }
    }

    /*CheckNetworkConnection*/
    private fun checkNetworkConnection() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.noconnection)
        builder.setTitle("No Internet Connection")
        builder.setMessage("Please turn on Internet Connection to Continue")
        builder.setNegativeButton("Ok") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    val isNetworkConnectionAvailable: Unit
        @SuppressLint("MissingPermission")
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnected
            if (isConnected) {
                Log.d("Network", "Connected")
            } else {
                checkNetworkConnection()
                Log.d("Network", "Not Connected")
            }
        }

    //Permission function starts from here
    private fun RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(
            this@StartActivity, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            ), RequestPermissionCode
        )
    }

    // overriden method.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RequestPermissionCode) {
            if (grantResults.size > 0) {
                val LocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val RExternalStoragePermission =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                val WExternalStoragePermission =
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                val Location2Permission = grantResults[3] == PackageManager.PERMISSION_GRANTED
                val ntwrkstatePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED
                if (LocationPermission && RExternalStoragePermission && WExternalStoragePermission && Location2Permission && ntwrkstatePermission) {
                    Toast.makeText(this@StartActivity, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this@StartActivity, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    //  Checking permission is enabled or not using function starts from here.
    fun CheckingPermissionIsEnabledOrNot(): Boolean {
        val SecondPermissionResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val ThirdPermissionResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val ForthPermissionResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val FifthPermissionResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val sixthPermissionResult = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        return SecondPermissionResult == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED && ForthPermissionResult == PackageManager.PERMISSION_GRANTED && FifthPermissionResult == PackageManager.PERMISSION_GRANTED && sixthPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        /*Splash variables declared here*/
        const val RequestPermissionCode = 7
    }


    internal class serverResponseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == "GET_SERVER_DATA") {
                val result = intent.getLongExtra(Constants.RESPONSE_STRING, 0)

                if (result > 0) {
                    StartActivity().updateUI()
                    //   Toast.makeText(context, context?.getText(R.string.data_recieved_successfully), Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_SHORT).show()
                }

                // Show it in GraphView
            }
        }
    }

    fun updateUI() {
        this@StartActivity.runOnUiThread {

            try {
                binding!!.btnLetstart.isClickable = true
                binding!!.btnLetstart.isFocusable = true
                binding!!.btnLetstart.isEnabled = true
                binding!!.view.visibility = View.GONE
                prefs!!.edit().putBoolean("firstrun", false).apply()
            } catch (e: Exception) {

            }

        }
    }


}