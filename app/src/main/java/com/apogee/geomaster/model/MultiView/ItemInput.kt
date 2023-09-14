package com.apogee.geomaster.model.MultiView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import java.util.Locale



class ItemInput(itemView: View, private val onItemValueListener: OnItemValueListener, context: Context) :
    RecyclerView.ViewHolder(itemView) {

    val txtinput: TextView = itemView.findViewById(R.id.txtinput)
    val edinput: EditText = itemView.findViewById(R.id.edinput)
    val cbNmea: CheckBox = itemView.findViewById(R.id.cbNmea)

    private var finaltext: String = ""
    private var title: String = ""
    private var model: String = ""

    init {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        model = sharedPreferences.getString(Constants.MODEL, "") ?: ""

        /*Text change listener*/
        val focusListener = MyFocusListenerImpl(onItemValueListener)
        edinput.onFocusChangeListener = focusListener
        edinput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                finaltext = s.toString()
                title = txtinput.text.toString()
            }
        })
    }

    /*Focus event listener*/
    private inner class MyFocusListenerImpl(private val onItemValueListener: OnItemValueListener) :
        View.OnFocusChangeListener {

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (!hasFocus) {
                val databaseOperation = DatabaseRepsoitory(itemView.context)

                val rmkvalue = databaseOperation.retrnfromtfromrmrk(title)
                val inputhint = databaseOperation.inputhint(title)

                try {
                    if (inputhint != null) {
                        when {
                            inputhint.equals("cm", ignoreCase = true) -> {
                                val value1 = Integer.parseInt(finaltext) * 100
                                val value = Integer.toString(value1, 16).toUpperCase()
                                Log.d("value===", value)
                                finaltext = bytesToHex(intToLittleEndian(value1.toLong())).toUpperCase()
                                onItemValueListener.returnValue(title, finaltext)
                                edinput.clearFocus()
                            }
                            inputhint.equals("Min", ignoreCase = true) -> {
                                val value1 = Integer.parseInt(finaltext) * 60
                                finaltext = bytesToHex(intToLittleEndian(value1.toLong())).toUpperCase()
                                onItemValueListener.returnValue(title, finaltext)
                                edinput.clearFocus()
                            }
                            finaltext != null && (title.contains("GGA") || title.contains("GSV") ||
                                    title.contains("GLL") || title.contains("GRS") || title.contains("GSA") ||
                                    title.contains("GST") || title.contains("RMC") || title.contains("VTG") ||
                                    title.contains("ZDA")) -> {
                                finaltext = Integer.toHexString(Integer.parseInt(finaltext))
                                onItemValueListener.returnValue(title, finaltext)
                                edinput.clearFocus()
                            }
                            finaltext != null && !title.contains("IP") && !title.contains("Mount Point") &&
                                    !title.contains("Mount-Point") && !title.contains("Password") &&
                                    !title.contains("Port") && !title.contains("File Name") &&
                                    !title.contains("Time") && !title.contains("Folder Name") &&
                                    !title.contains("Antenna Height") && !title.contains("SSID") &&
                                    !title.contains("PWD") && !title.contains("Username") &&
                                    !title.contains("NTRIP Password") && !title.contains("SSID Password") &&
                                    !title.contains("Frequency.") -> {
                                if (!model.isEmpty() && (model == "Navik100" || model == "TNAVIK50")) {
                                    if (finaltext.contains(".")) {
                                        finaltext = finaltext.replace(".", "")
                                    }
                                    val value1 = java.lang.Long.parseLong(finaltext)
                                    finaltext = bytesToHex(intToLittleEndian(value1)).toUpperCase()
                                }
                                onItemValueListener.returnValue(title, finaltext)
                                edinput.clearFocus()
                            }
                            else -> {
                                onItemValueListener.returnValue(title, hexString(finaltext))
                                edinput.clearFocus()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        fun bytesToHex(bytes: ByteArray): String {
            val builder = StringBuilder()
            for (b in bytes) {
                builder.append(String.format("%02x", b))
            }
            return builder.toString()
        }

        fun intToLittleEndian(numero: Long): ByteArray {
            val b = ByteArray(4)
            b[0] = (numero and 0xFF).toByte()
            b[1] = (numero shr 8 and 0xFF).toByte()
            b[2] = (numero shr 16 and 0xFF).toByte()
            b[3] = (numero shr 24 and 0xFF).toByte()
            return b
        }

        fun hexString(input: String): String {
            val charinput = input.toCharArray()
            val stringBuilder = StringBuilder()
            for (i in charinput.indices) {
                stringBuilder.append(Integer.toHexString(charinput[i].toInt()))
            }
            return stringBuilder.toString()
        }
    }
}
