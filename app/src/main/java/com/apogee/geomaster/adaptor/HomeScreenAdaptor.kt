package com.apogee.geomaster.adaptor


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.HomeScreenItemLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.utils.OnItemClickListener

class HomeScreenAdaptor(private val listener: OnItemClickListener) :
    ListAdapter<HomeScreenOption, HomeScreenAdaptor.HomeViewHolder>(callback) {

    inner class HomeViewHolder(private val binding: HomeScreenItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getItem(data: HomeScreenOption, listener: OnItemClickListener) {
            binding.imgTxt.setImageResource(data.icon)
            binding.profileTxt.text=data.title
            binding.iconCard.setOnClickListener{
                listener.onClickListener(data)
            }
        }
    }

    companion object {
        val callback = object : DiffUtil.ItemCallback<HomeScreenOption>() {
            override fun areItemsTheSame(
                oldItem: HomeScreenOption,
                newItem: HomeScreenOption
            ): Boolean {
                return oldItem.icon == newItem.icon
            }

            override fun areContentsTheSame(
                oldItem: HomeScreenOption,
                newItem: HomeScreenOption
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = HomeScreenItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.enter_anim_layout)
        holder.itemView.startAnimation(animation)
        item?.let {
           holder.getItem(it,listener)
        }

    }
}