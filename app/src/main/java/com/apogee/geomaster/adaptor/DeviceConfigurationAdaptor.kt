package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.databinding.DeviceConfigItemLayoutBinding
import com.apogee.geomaster.model.DeviceWorkMode

typealias DeviceWorkModeListener = (data: DeviceWorkMode) -> Unit

class DeviceConfigurationAdaptor(private val itemClicked: DeviceWorkModeListener) :
    ListAdapter<DeviceWorkMode, DeviceConfigurationAdaptor.DeviceWorkModeViewHolder>(diffUtil) {
    inner class DeviceWorkModeViewHolder(private val binding: DeviceConfigItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: DeviceWorkMode, itemClicked: DeviceWorkModeListener) {
            binding.projectName.text = data.type
            binding.projectName.append("\n\nWork Mode")
            data.information.forEach {
                binding.projectInfo.append("$it\n\n")
            }
            binding.cardView.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DeviceWorkMode>() {
            override fun areItemsTheSame(
                oldItem: DeviceWorkMode,
                newItem: DeviceWorkMode
            ) = oldItem.type == newItem.type

            override fun areContentsTheSame(
                oldItem: DeviceWorkMode,
                newItem: DeviceWorkMode
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