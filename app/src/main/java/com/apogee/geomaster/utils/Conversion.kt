package com.apogee.geomaster.utils

import android.content.Context
import android.util.Base64
import android.widget.Toast

class Conversion (context: Context){
    fun hextToString(hex: String): StringBuilder {
        val output = StringBuilder()
        var i = 0
        while (i < hex.length) {
            val str = hex.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output
    }
    fun hexString(input: String): String {
        val charinput = input.toCharArray()
        val stringBuilder = java.lang.StringBuilder()
        for (i in charinput.indices) {
            stringBuilder.append(Integer.toHexString(charinput[i].toInt()))
        }
        return stringBuilder.toString()
    }


    fun stringtohex(str: String): String {
        val ch = str.toCharArray()

        val sb = StringBuilder()
        for (i in ch.indices) {
            sb.append(Integer.toHexString(ch[i].toInt()))
        }
        return sb.toString()
    }



     fun stringToBase64(data : String):String
    {

        val dataa = data.toByteArray(charset("UTF-8"))
        val base64 = Base64.encodeToString(dataa, Base64.DEFAULT)

        return base64
    }
    fun convertStringToHex(str: String): String {
        val stringBuilder = java.lang.StringBuilder()
        val charArray = str.toCharArray()
        for (c in charArray) {
            val charToHex = Integer.toHexString(c.toInt())
            stringBuilder.append(charToHex)
        }
        println("Converted Hex from String: $stringBuilder")
        return stringBuilder.toString()
    }



}