package com.apogee.geomaster.model.MultiView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R


class inputonlytext(itemView: View, private val onItemValueListener: OnItemValueListener) :
    RecyclerView.ViewHolder(itemView) {

    val txtheader: TextView = itemView.findViewById(R.id.txtinput)
     val txtval: TextView = itemView.findViewById(R.id.txtinput2)

    init {
        itemView.setOnLongClickListener {
            onItemValueListener.returnValue(
                txtheader.text.toString(),
                txtval.text.toString(),
                adapterPosition,
                "",
                ""
            )
            false
        }
    }

    fun setBackgroundView(position: Int) {
        // Implement your logic for setBackgroundView if needed
    }
}
