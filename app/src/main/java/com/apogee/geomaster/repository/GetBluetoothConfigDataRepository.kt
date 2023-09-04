package com.apogee.geomaster.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.apogee.apilibrary.ApiCall
import com.apogee.apilibrary.Interfaces.CustomCallback
import com.apogee.databasemodule.DatabaseSingleton
import com.apogee.databasemodule.TableCreator
import com.apogee.geomaster.service.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class GetBluetoothConfigDataRepository(private val context: Context) : CustomCallback {

    val TAG = "CommunicationFragment"

    private lateinit var dbControl: DatabaseRepsoitory

    init {

        dbControl = DatabaseRepsoitory(context)
        Log.d(TAG, "onViewCreated: Bluetooth")


    }

    val database by lazy {
        DatabaseSingleton.getInstance(context).getDatabase()!!
    }

    val tableCreator = TableCreator(database)

    private var coroutineScope = CoroutineScope(Dispatchers.IO)


    private val _getBlutoothData = MutableStateFlow<Any?>(null)

    val getBlutoothData: MutableStateFlow<Any?>
        get() = _getBlutoothData


    fun getConfigData(deviceName: String) {
        ApiCall().postDataWithBody(
            deviceName,
            this,
            "http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getBluetoothConfigurationData/",
            50
        )
    }

    override fun onResponse(p0: Call<*>?, response: Response<*>?, p2: Int) {
        val responseBody = response?.body() as ResponseBody?

        if (response!!.isSuccessful) {
            if (responseBody != null) {
                try {
                    coroutineScope.launch {
                    val responseString = responseBody.string()
                    Log.d(TAG, "onResponse:Bluetooth $responseString")
                    async(Dispatchers.IO) {
                        dbControl.BluetoothConfigurationData(responseString)
                    }.await()


                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context)
                    val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                    editor.putString(Constants.BLUTOOTH_RESPONSE_STRING, responseString)
                    editor.apply()


                        _getBlutoothData.value = "Data Inserted Successfully"

                    }


                } catch (e: Exception) {
                    Log.d(TAG, "onResponse:Bluetooth Error ${e.message}")
                }
            }
        } else {
            val strOutput = response.toString()
        }

    }

    fun getModelId(device_name: String): List<String>?
    {
        var query=" select module_device_id from device_map dm,device d1,device d2,model m1,model m2" +
                " where dm.active='Y' and d1.active='Y' and m1.active='Y' and m2.active='Y' " +
                " and dm.finished_device_id=d1.id and dm.module_device_id=d2.id and d1.model_id=m1.id and d2.model_id=m2.id and m2.model_type_id='2'  and m1.device_no='"+device_name+"' "
        val data = tableCreator.executeStaticQuery(query)
        return data
    }

    fun getServiceIds(device_id: String): List<String>? {
        val data = tableCreator.executeStaticQuery("SELECT service_uuid FROM services where device_id = '"+device_id+"'")

        return data
    }
    fun getModelName(device_name : String): List<String>? {
        val data = tableCreator.executeStaticQuery("SELECT model_id FROM model where device_no = '"+device_name+"' ")

        return data
    }

    fun getCharacteristicIds(device_id : String): List<String>? {
        val uuidRead = tableCreator.executeStaticQuery("SELECT uuid,char_name FROM charachtristics where device_id = '"+device_id+"' ")

        return uuidRead
    }




    override fun onFailure(p0: Call<*>?, p1: Throwable?, p2: Int) {
        Log.d(TAG, "onFailure: " + p1!!.message)
    }

}