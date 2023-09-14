package com.apogee.geomaster.model.MultiView

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.repository.DatabaseRepsoitory
import com.apogee.geomaster.service.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder

class RecyclerlViewAdapter(
    private val itemTypeList: List<ItemType>,
    private val onItemValueListener: OnItemValueListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var context: Context
    private var lat = ""
    private var lng = ""
    private var alti = ""
    private var accuracy = ""
    private var clickListerner: ClickListerner? = null
    var selectedposition = -2
    private lateinit var dbControl: DatabaseRepsoitory



    interface ClickListerner {
        fun onSuccess(itemType: ItemType)
    }

    fun setListerner(clickListerner: ClickListerner) {
        this.clickListerner = clickListerner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            0 -> {
                val itemdropview =
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_row_dropdown, parent, false)
                ItemDropword(itemdropview)
            }
            1 -> {
                val iteminputview =
                    LayoutInflater.from(parent.context).inflate(R.layout.layout_row_input, parent, false)
                ItemInput(iteminputview, onItemValueListener, context)
            }

            3 -> {
                val ontxt = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_text, parent, false)
                inputonlytext(ontxt, onItemValueListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemType = itemTypeList[position]
        when (itemType.type1) {
            ItemType.DROPDOWNTYPE -> {
                val itemDropword = holder as ItemDropword
                itemDropword.txtdrop.text = itemType.title1
                itemDropword.setDropdown(itemType.stringStringMapdrop!!, onItemValueListener)
            }
            ItemType.INPUTTYPE -> {
                val itemInput = holder as ItemInput
                dbControl = DatabaseRepsoitory(context)
                val rmkvalue = dbControl.retrnfromtfromrmrk(itemType.title1)
                val inputhint = dbControl.inputhint(itemType.title1)
                itemInput.cbNmea.isChecked = itemType.isSelected

                if (itemType.isFromNmea) {
                    itemInput.cbNmea.visibility = View.VISIBLE
                    if (itemType.isSelected && itemType.oprtr == "false" && itemType.code == "true") {
                        itemInput.edinput.visibility = View.VISIBLE
                    } else {
                        itemInput.edinput.visibility = View.GONE
                    }
                } else {
                    itemInput.cbNmea.visibility = View.GONE
                    itemInput.edinput.visibility = View.VISIBLE
                }

                itemInput.cbNmea.setOnClickListener { v ->
                    if ((v as CheckBox).isChecked) {
                        itemInput.edinput.visibility = View.VISIBLE
                        onItemValueListener.returnValue(itemType.title1, false.toString())
                    } else {
                        itemInput.edinput.visibility = View.GONE
                        onItemValueListener.returnValue(itemType.title1, true.toString())
                    }
                }

                if (rmkvalue != null) {
                    if (rmkvalue == "1" || itemType.title1 == "Antenna Height" || itemType.title1 == "Time") {
                        itemInput.edinput.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    }
                }
                if (itemType.title1 == "Time") {
                    itemInput.edinput.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                        override fun afterTextChanged(editable: Editable?) {
                            if (editable != null && editable.toString() != "") {
                                try {
                                    if (Integer.parseInt(editable.toString()) < 3) {
                                        itemInput.edinput.error = "Time should be greater than 3"
                                    }
                                } catch (e: Exception) {
                                }
                            }
                        }
                    })
                }

                if (itemType.title1 == "Latitude" && lat != "" && !lat.isEmpty()) {
                    itemInput.edinput.setText(lat)
                    onItemValueListener.returnValue(itemType.title1.trim(), hexString(lat.trim()))
                } else if (itemType.title1 == "Longitude" && lng != "" && !lng.isEmpty()) {
                    itemInput.edinput.setText(lng)
                    onItemValueListener.returnValue(itemType.title1.trim(), hexString(lng.trim()))
                } else if (itemType.title1.contains("Altitude") && alti != "" && !alti.isEmpty()) {
                    itemInput.edinput.setText(alti)
                    onItemValueListener.returnValue(itemType.title1.trim(), hexString(alti.trim()))
                } else if (itemType.title1 == "Accuracy" && accuracy != "" && !accuracy.isEmpty()) {
                    itemInput.edinput.setText((hexToInt(accuracy) / 100).toString())
                    onItemValueListener.returnValue(itemType.title1, (hexToInt(accuracy.trim()) / 100).toString())
                } else {
                    itemInput.edinput.text = null
                }
                if (inputhint != null) {
                    itemInput.edinput.hint = inputhint
                }
                if (itemType.code != null) {
                    itemInput.edinput.setText(itemType.code)
                }
                if (itemType.title1 == "IP" || itemType.title1 == "Port" || itemType.title1 == "Latitude" ||
                    itemType.title1 == "Longitude" || itemType.title1 == "Altitude" || itemType.title1 == "Accuracy"
                ) {
                    itemInput.edinput.keyListener = DigitsKeyListener.getInstance("0123456789.")
                }
                if (itemType.title1 == "File Name" || itemType.title1 == "Folder Name") {
                    itemInput.edinput.filters = arrayOf(InputFilter.LengthFilter(18))
                }
                if (itemType.title1 == "Latitude" || itemType.title1 == "Longitude" || itemType.title1 == "Altitude") {
                    itemInput.edinput.filters = arrayOf(InputFilter.LengthFilter(10))
                    itemInput.edinput.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
                }
                if (itemType.title1 == "Username" || itemType.title1 == "Password" ||
                    itemType.title1 == "NTRIP Password" || itemType.title1 == "Mount-Point" ||
                    itemType.title1 == "PWD" || itemType.title1 == "SSID" || itemType.title1 == "SSID Password"
                ) {
                    itemInput.edinput.filters = arrayOf(InputFilter.LengthFilter(18))
                    itemInput.edinput.inputType = InputType.TYPE_CLASS_TEXT
                }
                if (itemType.title1 == "Altitude") {
                    val sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
                    itemInput.txtinput.text = itemType.title1 + "(" + sharedPreferences.getString(
                        Constants.ELEVATION, "") + ")"
                } else {
                    itemInput.txtinput.text = itemType.title1.trim()
                }
            }
/*
            ItemType.INPUTTYPEPROJECT -> {
                val inputProjectlist = holder as InputProjectlist
                inputProjectlist.txtpname.text = itemType.title
                inputProjectlist.txtoprtr.text = itemType.oprtr
                inputProjectlist.txttime.text = itemType.time
                inputProjectlist.tvZone.text = "Zone : " + itemType.zone
                inputProjectlist.setBackgroundView(position)
                val operation = itemType.oprtr
                val elevation = itemType.elevation
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                val p_name = sharedPreferences.getString(Constants.PROJECT_NAME, "")
                val elevations = sharedPreferences.getString(Constants.ELEVATION, "")
                if (itemType.title == p_name) {
                    inputProjectlist.llView.visibility = View.VISIBLE
                } else {
                    inputProjectlist.llView.visibility = View.GONE
                }
                inputProjectlist.itemView.setOnLongClickListener {
                    selectedposition = position
                    onItemValueListener.returnValue(
                        inputProjectlist.txtpname.text.toString() + "##longclick",
                        inputProjectlist.txttime.text.toString(),
                        position,
                        operation,
                        elevation
                    )
                    notifyDataSetChanged()
                    false
                }
                inputProjectlist.itemView.setOnClickListener {
                    selectedposition = position
                    onItemValueListener.returnValue(
                        inputProjectlist.txtpname.text.toString() + "##onclick##",
                        inputProjectlist.txttime.text.toString(),
                        position,
                        operation,
                        elevation
                    )
                    notifyDataSetChanged()
                }
                inputProjectlist.llView.setOnClickListener {
                    clickListerner?.onSuccess(itemType)
                }
            }
*/
            ItemType.INPUTONLYTEXT -> {
                val onlytxt = holder as inputonlytext
                onlytxt.txtheader.text = itemType.title1.trim()
                onlytxt.txtval.text = itemType.oprtr!!.trim()
                onlytxt.setBackgroundView(position)
                val operat = itemType.oprtr!!.trim()
                if (selectedposition != position) {
                    onlytxt.itemView.setBackgroundColor(
                        onlytxt.itemView.context.resources.getColor(
                            R.color.white
                        )
                    )
                } else {
                    onlytxt.itemView.setBackgroundColor(
                        onlytxt.itemView.context.resources.getColor(
                            R.color.light_gray
                        )
                    )
                }
                onlytxt.itemView.setOnClickListener {
                    selectedposition = position
                    onItemValueListener.returnValue(
                        onlytxt.txtheader.text.toString().trim(),
                        onlytxt.txtval.text.toString().trim(),
                        position,
                        operat,
                        itemType.elevation1
                    )
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemTypeList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemTypeList[position].type1) {
            0 -> 0
            1 -> 1
            2 -> 2
            else -> 3
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setListAdapter(latitude: String, longtitude: String, altitude: String, accuracy: String) {
        lat = latitude
        lng = longtitude
        alti = altitude
        this.accuracy = accuracy
        notifyDataSetChanged()
    }

    private fun hexString(input: String): String {
        val charinput = input.toCharArray()
        val stringBuilder = StringBuilder()
        for (i in charinput.indices) {
            stringBuilder.append(Integer.toHexString(charinput[i].toInt()))
        }
        return stringBuilder.toString()
    }

    private fun hexToInt(hex: String): Int {
        // Parse hex to int
        var hexValue = hex
        if (hex.contains(".")) {
            hexValue = hex.replace(".", "")
        }
        val value = Integer.parseInt(hexValue, 16)
        // Flip byte order using ByteBuffer
        val buffer = ByteBuffer.allocate(4)
        buffer.order(ByteOrder.BIG_ENDIAN)
        buffer.asIntBuffer().put(value)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        return buffer.asIntBuffer().get()
    }

/*    fun clear() {
        val size = itemTypeList.size
        itemTypeList.clear()
        notifyItemRangeRemoved(0, size)
    }*/
}