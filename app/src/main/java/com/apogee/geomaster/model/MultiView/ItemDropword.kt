package com.apogee.geomaster.model.MultiView

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R


class ItemDropword(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var txtdrop: TextView
    var spindrop: Spinner
    var title: String? = null

    init {
        txtdrop = itemView.findViewById<TextView>(R.id.txtdrop)
        spindrop = itemView.findViewById<Spinner>(R.id.spindrop)
    }

    fun setDropdown(listdropdown: List<String>?, onItemValueListener: OnItemValueListener) {
        spindrop.adapter = ArrayAdapter<String>(
            itemView.context,
            android.R.layout.simple_dropdown_item_1line,
            listdropdown!!
        )
        spindrop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val itemname = parent.getItemAtPosition(position).toString()
                title = txtdrop.text.toString()
                onItemValueListener.returnValue(title, itemname)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun setDropdown(listdropdown: Map<String, String?>, onItemValueListener: OnItemValueListener) {
        val baudratekey = listdropdown.keys
        val baudratevaluelist: List<String> = ArrayList(baudratekey)
        spindrop.adapter = ArrayAdapter<String>(
            itemView.context,
            android.R.layout.simple_dropdown_item_1line,
            baudratevaluelist
        )
        if (baudratevaluelist.contains("433.125 MHz")) {
            spindrop.setSelection(1)
        }
        spindrop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val itemname = parent.getItemAtPosition(position).toString()
                title = txtdrop.text.toString()
                val itemValue = listdropdown[itemname]
                Log.d("TAG", "onItemSelected: " + spindrop.selectedItem.toString())
                onItemValueListener.returnValue(title, itemValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun hexString(input: String): String {
        val charinput = input.toCharArray()
        val stringBuilder = StringBuilder()
        for (i in charinput.indices) {
            stringBuilder.append(Integer.toHexString(charinput[i].code))
        }
        return stringBuilder.toString()
    }
}
