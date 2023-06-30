package com.apogee.geomaster.service



internal object Constants {
    const val RESPONSE_STRING: String="responseString"
    const val INTENT_ACTION_DISCONNECT = /*BuildConfig.APPLICATION_ID +*/ "com.example.pda.Disconnect"
    const val NOTIFICATION_CHANNEL = /*BuildConfig.APPLICATION_ID +*/ "com.example.pda.Channel"
    const val INTENT_CLASS_MAIN_ACTIVITY =/*BuildConfig.APPLICATION_ID+*/ "com.example.pda.MainActivity"

    // values have to be unique within each app
    const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001
    const val DEVICE_NAME = "device_name"
    const val DEVICE_ADDRESSS = "device_address"
    const val DEVICE_ID = "device_id"
    const val DGPS_DEVICE_ID = "dgps_device_id"
    const val DGPS_DEVICE_ID_FOR_RADIO = "dgps_device_id_radio"
    const val MODULE_DEVICE = "module_device"
    const val antennapref = "antenapref"

    const val MAKE = "isMake"
    const val MODEL = "model"
    const val PROFILENAME = "profile_name"





}