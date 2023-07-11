package com.apogee.geomaster.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apogee.databasemodule.DatabaseSingleton
import com.apogee.databasemodule.TableCreator
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ActivityStartBinding
import com.apogee.geomaster.service.Constants
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class StartActivity : AppCompatActivity() {
    var TAG: String = StartActivity::class.java.simpleName
    var binding: ActivityStartBinding? = null
    var responseString = ""
    private var prefs: SharedPreferences? = null
    var sharedPreferences: SharedPreferences? = null

    //    private lateinit var database: SQLiteDatabase
    private lateinit var receiver: serverResponseReceiver
    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        RequestMultiplePermission()
        database = DatabaseSingleton.getInstance(this).getDatabase()!!
        val tableCreator = TableCreator(database)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@StartActivity)
        CheckingPermissionIsEnabledOrNot()
//************************************************************************************************************************************************
        val tableName1 = "ble_operation_name"
        val columns1 = arrayOf(
            TableCreator.ColumnDetails("ble_operation_name_id", "INTEGER", true),
            TableCreator.ColumnDetails("bleOperation_name", "TEXT", unique = true),
            TableCreator.ColumnDetails("remark", "STRING"))
        val tableCreation1 = tableCreator.createMainTableIfNeeded(tableName1, columns1)
        Log.d(TAG, "onCreate:tableCreation $tableCreation1")

        val tableName2 = "operation_name"
        val columns2 = arrayOf(
            TableCreator.ColumnDetails("operation_id", "INTEGER", true),
            TableCreator.ColumnDetails("operationName", "TEXT"),
            TableCreator.ColumnDetails("parent_id", "INTEGER"),
            TableCreator.ColumnDetails("id", "INTEGER"),
            TableCreator.ColumnDetails("is_super_child", "STRING"),
            TableCreator.ColumnDetails("remark", "STRING"),
//            TableCreator.ColumnDetails("sdf", "INTEGER", false, false, false, "", false)
        )
        val tableCreation2 = tableCreator.createMainTableIfNeeded(tableName2, columns2)
        Log.d(TAG, "onCreate:tableCreation $tableCreation2")


        val tableName3 = "input"
        val columns3 = arrayOf(
            TableCreator.ColumnDetails("input_id", "INTEGER"),
            TableCreator.ColumnDetails("command_id", "INTEGER"),
            TableCreator.ColumnDetails("response_id", "INTEGER"),
            TableCreator.ColumnDetails("parameter_id", "INTEGER"),
            TableCreator.ColumnDetails("remark", "STRING"),
//            TableCreator.ColumnDetails("sdf", "INTEGER", false, false, false, "", false)
        )
        val tableCreation3 = tableCreator.createMainTableIfNeeded(tableName3, columns3)
        Log.d(TAG, "onCreate:tableCreation $tableCreation3")


        val tableName4 = "command_type"
        val columns4 = arrayOf(
            TableCreator.ColumnDetails("command_type_id", "INTEGER", true),
            TableCreator.ColumnDetails("name", "TEXT", unique = true),
            TableCreator.ColumnDetails("remark", "STRING"),
            TableCreator.ColumnDetails("id", "INTEGER"),)
        val tableCreation4 = tableCreator.createMainTableIfNeeded(tableName4, columns4)
        Log.d(TAG, "onCreate:tableCreation $tableCreation4")


//************************************************************************************************************************************************

        receiver = serverResponseReceiver()

        /*Saving package name for check first time installation*/

        binding?.btnLetstart?.setOnClickListener {
//            val intents = Intent(this@StartActivity, MainActivity::class.java)
//            startActivity(intents)
//            finish()
//            responseString = sharedPreferences!!.getString(Constants.RESPONSE_STRING, "").toString()
//            if (!responseString.equals("")) {
//                Log.d(TAG, "onCreate:IF responseString $responseString")
//
//
//            }

            var jsonString = readJSONFromAsset()
            val jsonObject = JSONObject(jsonString)
            for (key in jsonObject.keys()) {
                var dataList: MutableList<ContentValues> = ArrayList()
                val jsonArray = jsonObject.getJSONArray(key)
                try {
                    for (i in 0 until jsonArray.length()) {
                        dataList.clear()
                        val jsonObject1: JSONObject = jsonArray.getJSONObject(i)

                        val iter: Iterator<String> = jsonObject1.keys()
                        val values1 = ContentValues()
                        while (iter.hasNext()) {
                            val keyss = iter.next()
                            try {
                                val valueddd: Any = jsonObject1.get(keyss)
                                values1.put(keyss, valueddd.toString())
                            } catch (e: JSONException) {
                                Log.d(TAG, "onCreate: ${e.message}")
                            }
                        }
                        dataList.add(values1)
                        val result=tableCreator.insertDataIntoTable(key.toString(), dataList)
                        Log.d(TAG, "onCreate: result:--$result")
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onCreate: Exception " + e.message)
                }
            }


        }
//        binding!!.abc.setOnClickListener {
//            val status=useJoins()
//            Log.d(TAG, "onCreate:Status $status")
//        }

    }

    fun useJoins(){
        val tableCreator = TableCreator(database)
        val columns=arrayOf("name")
        try {
            val sendMsg=tableCreator.WhereHavingClause("command_type",columns,"remark = 'abcddd'","","")
            Log.d(TAG, "useJoins: ${sendMsg}")
        }catch (e:Exception){
            Log.d(TAG, "useJoinsException : ${e.message} ")
        }

    }

    fun readJSONFromAsset(): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open("ResponseJson.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    /*Check first run in onResume*/
    override fun onResume() {
        super.onResume()
        binding!!.btnLetstart.isClickable = true
        isNetworkConnectionAvailable
//        val intentService = Intent(this@StartActivity, ApiService::class.java)
//        startService(intentService)
//            prefs!!.edit().putBoolean("firstrun", false).apply()

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
                val ntwrkstatePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED
                Log.d(
                    TAG, "onRequestPermissionsResult:" +
                            "ntwrkstatePermission : $ntwrkstatePermission \n"
                )
                if (ntwrkstatePermission) {
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
//                binding!!.view.visibility = View.GONE
                prefs!!.edit().putBoolean("firstrun", false).apply()
            } catch (e: Exception) {

            }

        }
    }


}