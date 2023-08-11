package com.apogee.geomaster.model

import com.apogee.geomaster.utils.getEmojiByUnicode

data class DeviceWorkMode(
    val type:String,
    val information:List<String>
) {
    companion object{
        val list= listOf(
            DeviceWorkMode(
                "Rover Device"
                , listOf(
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                    "Sample Work-mode data ${getEmojiByUnicode(0x2705)}",
                )
            )
        )
    }

}