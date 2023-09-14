package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.databinding.CommunctionItemLayoutBinding
import com.apogee.geomaster.model.NetworkConnection
import com.apogee.geomaster.model.RadioConnection
import com.apogee.geomaster.model.WifiConnection
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.setHtmlBoldTxt
import com.apogee.geomaster.utils.setHtmlTxt


class ConnectionAdaptor<T>(private val list: List<T>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ConnectionAdaptor<T>.ConnectionViewHolder>() {

    inner class ConnectionViewHolder(private val binding: CommunctionItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun getData(data: T) {
            if (data is RadioConnection) {
                binding.connectionInfo.append(setHtmlBoldTxt("Toggle Previous Configuration"))
                binding.connectionInfo.append(setHtmlTxt(data.togglePreviousConfiguration,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("DataRate"))
                binding.connectionInfo.append(setHtmlTxt(data.dataRate.first,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Power "))
                binding.connectionInfo.append(setHtmlTxt(data.power.first,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Frequency"))
                binding.connectionInfo.append(setHtmlTxt(data.frequency.first,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Baud Rate"))
                binding.connectionInfo.append(setHtmlTxt(data.baudRate.first,"'#215FA6'"))
                binding.connectionInfo.append("\n")
            }

            if (data is NetworkConnection) {
                binding.connectionInfo.append(setHtmlBoldTxt("AirData "))
                binding.connectionInfo.append(setHtmlTxt(data.airDataRate,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Protocol "))
                binding.connectionInfo.append(setHtmlTxt(data.protocol,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Power "))
                binding.connectionInfo.append(setHtmlTxt(data.power,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Toggle Previous Switch "))
                binding.connectionInfo.append(setHtmlTxt(data.togglePreviousButton,"'#215FA6'"))
                binding.connectionInfo.append("\n")
            }

            if (data is WifiConnection) {
                binding.connectionInfo.append(setHtmlBoldTxt("AirData "))
                binding.connectionInfo.append(setHtmlTxt(data.airDataRate,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Protocol "))
                binding.connectionInfo.append(setHtmlTxt(data.protocol,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Power "))
                binding.connectionInfo.append(setHtmlTxt(data.power,"'#215FA6'"))
                binding.connectionInfo.append("\n")
                binding.connectionInfo.append(setHtmlBoldTxt("Toggle Previous Switch "))
                binding.connectionInfo.append(setHtmlTxt(data.togglePreviousButton,"'#215FA6'"))
                binding.connectionInfo.append("\n")
            }

            binding.connectionInfo.setOnClickListener {
                listener.onClickListener(Pair(true, data))
            }
            binding.clearInfo.setOnClickListener {
                listener.onClickListener(Pair(false, data))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder {
        val binding = CommunctionItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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