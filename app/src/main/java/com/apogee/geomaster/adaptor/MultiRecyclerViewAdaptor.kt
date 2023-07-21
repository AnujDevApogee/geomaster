package com.apogee.geomaster.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.viewholder.MultiViewHolder
import com.apogee.geomaster.databinding.EditTextLayoutBinding
import com.apogee.geomaster.databinding.SpinnerDropdownLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import java.lang.IllegalArgumentException


class MultiRecyclerViewAdaptor : ListAdapter<DynamicViewType, MultiViewHolder>(diff) {

    companion object {
        val diff = object : DiffUtil.ItemCallback<DynamicViewType>() {
            override fun areItemsTheSame(
                oldItem: DynamicViewType,
                newItem: DynamicViewType
            ): Boolean {
                return getValue(oldItem) == getValue(newItem)
            }

            override fun areContentsTheSame(
                oldItem: DynamicViewType,
                newItem: DynamicViewType
            ): Boolean {
                return oldItem == newItem
            }

        }

        private fun getValue(data: DynamicViewType): String {
            return when (data) {
                is DynamicViewType.EditText -> data.hint
                is DynamicViewType.SpinnerData -> data.hint
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        return when (viewType) {
            R.layout.edit_text_layout -> {
                val binding = EditTextLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MultiViewHolder.EditTextViewHolder(binding)
            }

            R.layout.spinner_dropdown_layout -> {
                val binding =
                    SpinnerDropdownLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MultiViewHolder.DropDownViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown Layout")
        }
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let {
            when (holder) {
                is MultiViewHolder.DropDownViewHolder -> {
                    holder.bindIt(it as DynamicViewType.SpinnerData)
                }

                is MultiViewHolder.EditTextViewHolder -> {
                    holder.bindIt(it as DynamicViewType.EditText)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DynamicViewType.EditText -> R.layout.edit_text_layout
            is DynamicViewType.SpinnerData -> R.layout.spinner_dropdown_layout
            else -> 0
        }
    }
}