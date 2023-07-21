package com.apogee.geomaster.adaptor.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.apogee.geomaster.databinding.EditTextLayoutBinding
import com.apogee.geomaster.databinding.SpinnerDropdownLayoutBinding
import com.apogee.geomaster.model.DynamicViewType

sealed class MultiViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


    class DropDownViewHolder(private val binding: SpinnerDropdownLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.SpinnerData) {
            binding.spinnerTextInputLayout.hint = data.hint
            binding.datums.setText(data.hint)
        }

    }


    class EditTextViewHolder(private val binding: EditTextLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.EditText) {
            binding.ipEd.hint = data.hint
binding.ipEdLayout.hint=data.hint
        }

    }

}