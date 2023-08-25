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
import com.apogee.geomaster.databinding.ProjectListItemBinding
import com.apogee.geomaster.model.ConfigSetup
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.setHtmlTxt

class ConfigurationListAdapter (private val itemOnClickListener: OnItemClickListener) :
ListAdapter<ConfigSetup, ConfigurationListAdapter.ConfigurationViewModel>(diffUtils) {


    companion object {
        val diffUtils = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ConfigurationViewModel(private val binding: ProjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project, itemOnClickListener: OnItemClickListener, position: Int) {
            if (position==0){
                binding.projectName.text= setHtmlTxt(project.title,"'#FFB4AB'")
            }else{
                binding.projectName.text=project.title
            }
//            binding.projectInfo.text="Datum Name ${project.title}\n"
            binding.projectInfo.text=""
            binding.projectInfo.append("Configuration Name:-- ${project.configurationName}\n")
            /*            binding.projectInfo.append("Projection Type:-- ${project.projectionType}\n")
                        binding.projectInfo.append("Zone:--${project.zone}\n")*/
            Log.i(ContentValues.TAG, "bind: FromPROJECT")
            binding.cardView.setOnClickListener {
                itemOnClickListener.onClickListener(project)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigurationViewModel {
        val binding = ProjectListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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