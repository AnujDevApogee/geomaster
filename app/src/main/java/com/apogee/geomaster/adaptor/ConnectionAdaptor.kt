package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.databinding.CommunctionItemLayoutBinding
import com.apogee.geomaster.model.NetworkConnection
import com.apogee.geomaster.model.RadioConnection
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.setHtmlTxt


class ConnectionAdaptor<T>(private val list: List<T>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ConnectionAdaptor<T>.ConnectionViewHolder>() {

    inner class ConnectionViewHolder(private val binding: CommunctionItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun getData(data: T) {
            if (data is Map<*, *>) {
                data.forEach {
                    binding.connectionInfo.append(setHtmlBoldTxt("${it.key} "))
                    binding.connectionInfo.append(setHtmlTxt(getData(it.value).toString(),"'#215FA6'"))
                    binding.connectionInfo.append("\n")
                }

            }

            binding.connectionInfo.setOnClickListener {
                listener.onClickListener(Pair(true, data))
            }
            binding.clearInfo.setOnClickListener {
                listener.onClickListener(Pair(false, data))
            }
        }

    }

    private fun getData(value: Any?): Any? {
        if (value is String)
            return value
        if (value is Pair<*, *>)
            return value.first as String
        return  null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        val binding =
            CommunctionItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConnectionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        val item = list[position]
        holder.getData(item)
    }
}