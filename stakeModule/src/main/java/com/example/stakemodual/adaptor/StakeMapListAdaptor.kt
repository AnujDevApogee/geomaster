package com.example.stakemodual.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stakemodual.databinding.StakeOptionListLayoutBinding
import com.example.stakemodual.model.StakeMapLine
import com.example.stakemodual.utils.OnItemClickListener


class StakeMapListAdaptor(private val itemClicked: OnItemClickListener) :
    ListAdapter<StakeMapLine, StakeMapListAdaptor.StakePointViewHolder>(diffUtil) {
    inner class StakePointViewHolder(private val binding: StakeOptionListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: StakeMapLine, itemClicked: OnItemClickListener) {
            binding.imgBtn.setImageResource(data.id)
            binding.root.setOnClickListener {
                itemClicked.onClickListener(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<StakeMapLine>() {
            override fun areItemsTheSame(
                oldItem: StakeMapLine,
                newItem: StakeMapLine
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StakeMapLine,
                newItem: StakeMapLine
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StakePointViewHolder {
        val binding =
            StakeOptionListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StakePointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StakePointViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}