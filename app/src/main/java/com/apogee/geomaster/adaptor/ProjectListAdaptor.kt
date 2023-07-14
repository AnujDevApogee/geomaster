package com.apogee.geomaster.adaptor


import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ProjectListItemBinding
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.utils.OnItemClickListener


class ProjectListAdaptor(private val itemOnClickListener: OnItemClickListener) :
    ListAdapter<Project, ProjectListAdaptor.ProjectViewModel>(diffUtils) {


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

    inner class ProjectViewModel(private val binding: ProjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project, itemOnClickListener: OnItemClickListener) {
            binding.projectName.text=project.title
            binding.projectInfo.text="Datum Name ${project.dataumName}\n"
            binding.projectInfo.append("Datum Name ${project.dataumName}\n")
            binding.projectInfo.append("ElevationKey Name ${project.elevationKey}\n")
            binding.root.setOnClickListener {
                itemOnClickListener.onClickListener(project)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewModel {
        val binding = ProjectListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ProjectViewModel(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewModel, position: Int) {
        val item = getItem(position)
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.enter_anim_layout)
        holder.itemView.startAnimation(animation)
        item?.let {
            holder.bind(it,itemOnClickListener)
        }
    }


}