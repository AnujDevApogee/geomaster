package com.apogee.geomaster.broadcast


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log

class WifiReceiver(var wifiManager: WifiManager) : BroadcastReceiver() {
    var sb: StringBuilder? = null


    override fun onReceive(context: Context, intent: Intent) {

        val action = intent.action
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == action) {
            sb = StringBuilder()
            val wifiList = wifiManager.scanResults
            val deviceList = ArrayList<String>()
            for (scanResult in wifiList) {
                Log.d("TAG", "onReceive: "+scanResult)
                sb!!.append("\n").append(scanResult.SSID).append(" - ")
                    .append(scanResult.capabilities)
                if(scanResult.SSID.isNotEmpty()){
                    if(scanResult.frequency>=2412) {
                        deviceList.add(scanResult.SSID)
                    }
                }

            }
            val local = Intent()
            local.action = "service.to.activity.transfer"
            local.putExtra("deviceList", deviceList)
            context.sendBroadcast(local)

        }
    }

}