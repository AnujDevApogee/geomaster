package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apogee.geomaster.databinding.StakePointItemBinding
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog


class StakePointAdaptor(private val itemClicked: OnItemClickListener) :
    ListAdapter<SurveyModel, StakePointAdaptor.StakeViewHolder>(diffUtil) {

    private val asyncDiffUtil: AsyncListDiffer<SurveyModel>? = AsyncListDiffer(this, diffUtil)


    override fun getItemCount(): Int {
        return asyncDiffUtil?.currentList?.size ?: 0
    }

    override fun submitList(list: MutableList<SurveyModel>?) {
        createLog("ls_hit","from submit list ${list?.size}")
        asyncDiffUtil?.submitList(list)

    }
    inner class StakeViewHolder(private val binding: StakePointItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(data: SurveyModel, itemClicked: OnItemClickListener) {
            binding.text1.text = data.pointName
            binding.root.setOnClickListener {
                itemClicked.onClickListener(data)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SurveyModel>() {
            override fun areItemsTheSame(
                oldItem: SurveyModel,
                newItem: SurveyModel
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SurveyModel,
                newItem: SurveyModel
            ) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StakeViewHolder {
        val binding = StakePointItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StakeViewHolder(binding)
    }

    override fun getItem(position: Int): SurveyModel? {
        return asyncDiffUtil?.currentList?.get(position)
    }

    override fun onBindViewHolder(holder: StakeViewHolder, position: Int) {
        val currItem = getItem(position)
        currItem?.let {
            holder.setData(it, itemClicked)
        }
    }

}