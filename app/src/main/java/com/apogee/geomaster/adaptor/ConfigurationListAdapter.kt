package com.apogee.geomaster.adaptor

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ConfigListItemsBinding
import com.apogee.geomaster.databinding.ConfigListItemsBindingImpl
import com.apogee.geomaster.model.ConfigSetup
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.setHtmlTxt

class ConfigurationListAdapter (private val itemOnClickListener: OnItemClickListener) : ListAdapter<ConfigSetup, ConfigurationListAdapter.ConfigurationViewModel>(diffUtils) {


    companion object {
        val diffUtils = object : DiffUtil.ItemCallback<ConfigSetup>() {
            override fun areItemsTheSame(oldItem: ConfigSetup, newItem: ConfigSetup): Boolean {
                return oldItem.configurationName == newItem.configurationName
            }

            override fun areContentsTheSame(oldItem: ConfigSetup, newItem: ConfigSetup): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ConfigurationViewModel(private val binding: ConfigListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(config: ConfigSetup, itemOnClickListener: OnItemClickListener, position: Int) {
            if (position==0){
                binding.confiName.text= setHtmlTxt(config.configurationName,"'#FFB4AB'")
            }else{
                binding.confiName.text=config.configurationName
            }
//            binding.projectInfo.text="Datum Name ${project.title}\n"
            binding.configInfo.text=""
            binding.configInfo.append("Configuration Name:-- ${config.configurationName}\n")
            binding.configInfo.append("Datum Name:-- ${config.datumName}\n")
                        binding.configInfo.append("Work Mode:--${config.workMode}\n")

            Log.i(ContentValues.TAG, "bind: FromPROJECT")
            binding.cardView.setOnClickListener {
                itemOnClickListener.onClickListener(config)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationViewModel {
        val binding = ConfigListItemsBindingImpl.inflate(LayoutInflater.from(parent.context),parent,false)
        return ConfigurationViewModel(binding)
    }

    override fun onBindViewHolder(holder: ConfigurationViewModel, position: Int) {
        val item = getItem(position)
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.enter_anim_layout)
        holder.itemView.startAnimation(animation)
        item?.let {
            holder.bind(it,itemOnClickListener,position)
        }
    }



}
