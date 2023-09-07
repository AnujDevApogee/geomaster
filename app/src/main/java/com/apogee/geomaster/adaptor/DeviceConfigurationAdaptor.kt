package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.DeviceConfigItemLayoutBinding
import com.apogee.geomaster.model.DeviceMode
import com.apogee.geomaster.utils.setHtmlTxt
import com.permissionx.guolindev.dialog.permissionMapOnS

typealias DeviceWorkModeListener = (data: DeviceMode,position:Int) -> Unit

class DeviceConfigurationAdaptor(private val itemClicked: DeviceWorkModeListener) :
    ListAdapter<DeviceMode, DeviceConfigurationAdaptor.DeviceWorkModeViewHolder>(diffUtil) {
    var lastPosi=-1
    inner class DeviceWorkModeViewHolder(private val binding: DeviceConfigItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: DeviceMode, itemClicked: DeviceWorkModeListener) {
            binding.tvRecycler.text=data.type
            binding.rdMode.setImageResource(R.drawable.radio_off)

            binding.cardView.setOnClickListener {
                lastPosi=position
                binding.rdMode.setImageResource(R.drawable.radio_on)
                notifyDataSetChanged()

            }
            if(lastPosi==position){
                binding.rdMode.setImageResource(R.drawable.radio_on)
                itemClicked.invoke(data,lastPosi)

            }else{
                binding.rdMode.setImageResource(R.drawable.radio_off)
            }


        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DeviceMode>() {
            override fun areItemsTheSame(
                oldItem: DeviceMode,
                newItem: DeviceMode
            ) = oldItem.type == newItem.type

            override fun areContentsTheSame(
                oldItem: DeviceMode,
                newItem: DeviceMode
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceWorkModeViewHolder {
        val binding = DeviceConfigItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DeviceWorkModeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceWorkModeViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}