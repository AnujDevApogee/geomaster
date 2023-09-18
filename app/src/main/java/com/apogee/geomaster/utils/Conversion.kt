package com.apogee.geomaster.utils

import android.content.Context
import android.os.FileUtils
import android.util.Base64
import okhttp3.internal.and
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Locale

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



/*    fun appendLog(text: String?, filename: String?) {

        val newpath = "/storage/emulated/0/Android/data/com.apogee.geomaster/files"
        val dir = File(newpath, "/LogFile")
        val logFile = File(dir, filename)
        if (!logFile.exists()) {
            try {
                dir.mkdirs()
                //  logFile.createNewFile();
            } catch (e: java.lang.Exception) {
            }
        }
        try {
            FileUtils.write(logFile, text, true)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }*/

    /*THIS METHOD IS USED FOR CRC VALUE OF COMMAND*/
    fun checksum(command: String): String {
        val commandPair = arrayOfNulls<String>(command.length / 2 + 1)
        var j = 0
        val size = command.length
        run {
            var i = 0
            while (i < size) {
                commandPair[j] = command.substring(i, i + 2)
                j++
                i += 2
            }
        }
        var ch_A = "0"
        var ch_B = "0"
        val length = commandPair.size - 1
        for (i in 0 until length) {
            ch_A = commandPair[i]?.let { addCheckSum(ch_A, it) }!!
            ch_B = addCheckSum(ch_B, ch_A)
        }
        ch_A = Integer.toHexString(ch_A.toInt(16) and 0xFF).toUpperCase(Locale.ROOT)
        ch_B = Integer.toHexString(ch_B.toInt(16) and 0xFF).toUpperCase(Locale.ROOT)
        if (ch_A.length == 1) {
            ch_A = "0$ch_A"
        }
        if (ch_B.length == 1) {
            ch_B = "0$ch_B"
        }
        return ch_A + ch_B
    }

    /*Calculate checksum for the command*/
    fun addCheckSum(ch_A: String, ch_B: String): String {
        val A = ch_A.toInt(16)
        val B = ch_B.toInt(16)
        val sum = A + B
        return Integer.toHexString(sum)
    }
    fun appendLog(text: String?, filename: String?) {

        val newpath = "/storage/emulated/0/Android/data/com.apogee.geomaster/files"
        val dir = File(newpath, "/LogFile")
        val logFile = File(dir, filename)
        if (!logFile.exists()) {
            try {
                dir.mkdirs()
                //  logFile.createNewFile();
            } catch (e: java.lang.Exception) {
            }
        }
        try {
//            FileUtils.write(logFile, text, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun fromHexString(s: CharSequence): ByteArray {
        val buf = ByteArrayOutputStream()
        var b: Byte = 0
        var nibble = 0

        for (pos in 0 until s.length) {
            if (nibble == 2) {
                buf.write(b.toInt())
                nibble = 0
                b = 0
            }
            val c = s[pos].toInt()
            when {
                (c in '0'.toInt()..'9'.toInt()) -> {
                    nibble++
                    b = (b * 16 + (c - '0'.toInt())).toByte()
                }
                (c in 'A'.toInt()..'F'.toInt()) -> {
                    nibble++
                    b = (b * 16 + (c - 'A'.toInt() + 10)).toByte()
                }
                (c in 'a'.toInt()..'f'.toInt()) -> {
                    nibble++
                    b = (b * 16 + (c - 'a'.toInt() + 10)).toByte()
                }
            }
        }

        if (nibble > 0) {
            buf.write(b.toInt())
        }

        return buf.toByteArray()
    }

    fun calculateChecksum(command: String): String {
        // Convert hex string to byte array
        val bytes = ByteArray(command.length / 2)
        for (i in bytes.indices) {
            bytes[i] = command.substring(2 * i, 2 * i + 2).toInt(16).toByte()
        }
        // Calculate sum of all bytes
        var sum = 0
        for (b in bytes) {
            sum += b and 0xff
        }
        // return sum % 256;
        // Take sum modulo 256 to get checksum8 value
        val checksum = sum % 256
        // Convert checksum8 value to hexadecimal string in normal form
        return String.format("%02X", checksum)
    }
    fun toHexString(sb: java.lang.StringBuilder, buf: ByteArray) {
        toHexString(sb, buf, 0, buf.size)
    }

    fun toHexString(sb: java.lang.StringBuilder, buf: ByteArray, begin: Int, end: Int) {
        for (pos in begin until end) {
            if (sb.length > 0) sb.append(' ')
            var c: Int
            c = (buf[pos].toInt() and 0xff) / 16
            c += if (c >= 10) 'A'.code - 10 else '0'.code
            sb.append(c.toChar())
            c = (buf[pos].toInt() and 0xff) % 16
            c += if (c >= 10) 'A'.code - 10 else '0'.code
            sb.append(c.toChar())
        }
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