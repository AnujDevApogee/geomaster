package com.apogee.geomaster.adaptor


import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ProjectListItemBinding
import com.apogee.geomaster.model.Project
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.setHtmlTxt
import com.apogee.geomaster.utils.toastMsg


class ProjectListAdaptor(
    private val itemOnClickListener: OnItemClickListener,
    private val context: Activity
) :
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

        fun bind(project: Project, itemOnClickListener: OnItemClickListener,position: Int) {
             if (position==0){
                 binding.projectName.text= setHtmlTxt(project.title,"'#FFB4AB'")
            }else{
                 binding.projectName.text=project.title
             }
//            binding.projectInfo.text="Datum Name ${project.title}\n"
            binding.projectInfo.text = ""
            binding.projectInfo.append("Datum Name:-- ${project.dataumName}\n")
            binding.projectInfo.append("Projection Type:-- ${project.projectionType}\n")
            binding.projectInfo.append("Zone:--${project.zone}\n")
            Log.i(TAG, "bind: FromPROJECT")
            binding.cardView.setOnClickListener {
                itemOnClickListener.onClickListener(project)
            }

            binding.imgBtn.setOnClickListener {
                    popUpMenu()
            }
        }

        private fun popUpMenu() {
            val popUpMenu = PopupMenu(context,binding.imgBtn)
            popUpMenu.menuInflater.inflate(R.menu.project_mnu_selection,popUpMenu.menu)
            popUpMenu.show()
            popUpMenu.setOnMenuItemClickListener {
                itemOnClickListener.onClickListener(it)
                return@setOnMenuItemClickListener true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewModel {
        val binding = ProjectListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProjectViewModel(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewModel, position: Int) {
        val item = getItem(position)
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.enter_anim_layout)
        holder.itemView.startAnimation(animation)
        item?.let {
            holder.bind(it,itemOnClickListener,position)
        }
    }


}