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


    fun getServiceIds(deviceName: String): List<String>? {
        var query="select module_device_id from device_map dm,device d1,device d2,model m1,model m2 where dm.finished_device_id=d1.device_id and dm.module_device_id=d2.device_id and d1.model_id=m1.model_id and d2.model_id=m2.model_id and d2.device_type_id='3'  and m1.device_no='$deviceName'"
        Log.d(TAG, "getServiceIds: "+query)
        var id = tableCreator.executeStaticQuery(
            query
        )
        Log.d(TAG, "getServiceIds: id===$id")


        var serviceUUID = getServiceIdFromId(id)

        return serviceUUID
    }

    private fun getServiceIdFromId(id: List<String>?): List<String>? {

        var serviceUUId = tableCreator.executeStaticQuery("select s.service_uuid,s.services_id from services s,device d,model m , device_map dm,device_characteristic_ble_map dcbm,charachtristics c where dcbm.device_id=d.device_id and dcbm.read_characteristic_id=c.char_id and c.service_id=s.services_id and d.model_id=m.model_id  and dm.module_device_id=d.device_id  and d.device_id='${id!!.get(0)}' ")

//        var serviceId = tableCreator.executeStaticQuery("select s.services_id from services s,device d,model m , device_map dm,device_characteristic_ble_map dcbm,charachtristics c where dcbm.device_id=d.device_id and dcbm.read_characteristic_id=c.char_id and c.service_id=s.services_id and d.model_id=m.model_id  and dm.module_device_id=d.device_id  and d.device_id='${id!!.get(0)}' ")
//
//

//        getCharacteristicIds(serviceId)


        Log.d(TAG, "getServiceIdFromId: serviceID====$serviceUUId")
        return serviceUUId
    }

    fun getModelName(device_name: String): List<String>? {
        val data =
            tableCreator.executeStaticQuery("SELECT model_id FROM model where device_no = '" + device_name + "' ")

        return data
    }

    fun getCharacteristicIds(serviceId: String): List<String>? {
        val uuidRead = tableCreator.executeStaticQuery("SELECT uuid,char_name FROM charachtristics where service_id = '${serviceId}' ")
        Log.d(TAG, "getServiceIdFromId: uuidRead====$uuidRead")

        return uuidRead
    }


    override fun onFailure(p0: Call<*>?, p1: Throwable?, p2: Int) {
        Log.d(TAG, "onFailure: " + p1!!.message)
    }

}