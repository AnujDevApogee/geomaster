package com.apogee.geomaster.service

import com.apogee.geomaster.response_handling.model.ResponseHandlingModel
import java.text.DecimalFormat


internal object Constants {
    const val RESPONSE_STRING: String="responseString"
    const val BLUTOOTH_RESPONSE_STRING: String="bluetoothResponseString"
    const val FIRSTRUN: Boolean=false
    const val INTENT_ACTION_DISCONNECT = /*BuildConfig.APPLICATION_ID +*/ "com.example.pda.Disconnect"
    const val NOTIFICATION_CHANNEL = /*BuildConfig.APPLICATION_ID +*/ "com.example.pda.Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY =/*BuildConfig.APPLICATION_ID+*/ "com.example.pda.MainActivity"
    const val ELEVATION = "elevation"
    const val RADIO_TYPE = "radio_type"
    const val newline_crlf = "\r\n"


    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001
    const val DEVICE_NAME = "device_name"
    const val DEVICE_ADDRESSS = "device_address"
    const val DEVICE_ID = "device_id"
    const val DGPS_DEVICE_ID = "dgps_device_id"
    const val DGPS_DEVICE_ID_FOR_RADIO = "dgps_device_id_radio"
    const val GNSSMODULENAME = "gnssmodulename"
    const val MODULE_DEVICE = "module_device"

    const val antennapref = "antenapref"

    const val MAKE = "isMake"
    const val MODEL = "model"
    const val PROFILENAME = "profile_name"

    const val TRIMBLE_ProtocolKey = "trimble_protocolkey"
    const val TRIMBLE_ProtocolValue = "trimble_protocolValue"
    const val TRIMBLE_rs232Key = "trimble_rs232key"
    const val TRIMBLE_rs232Value = "trimble_rs232Value"

        val threeDecimalPlaces = DecimalFormat("0.000")
        val twoDecimalPlaces = DecimalFormat("0.00")
        val sevenDecimalPlaces = DecimalFormat("0.000000000")
        val fiveDecimalPlaces = DecimalFormat("0.00000")
        var gsvFlag = 0
        var gsaFlag = 0
        var gsv_res_map = ArrayList<ResponseHandlingModel>()
        var gsa_res_map = ArrayList<ResponseHandlingModel>()


    const val HEADER_LENGTH = "header_length"
    const val MOTHERBOARDID = "motherBoard_id"
    val HEADER_NAME: String = "header_name"

}