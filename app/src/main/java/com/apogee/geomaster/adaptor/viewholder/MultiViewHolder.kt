package com.apogee.geomaster.adaptor.viewholder

import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.apogee.geomaster.databinding.EditTextLayoutBinding
import com.apogee.geomaster.databinding.SpinnerDropdownLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.isInvalidString

sealed class MultiViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


    class DropDownViewHolder(private val binding: SpinnerDropdownLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.SpinnerData, itemClickListener: OnItemClickListener) {
            binding.spinnerTextInputLayout.hint = data.hint
            binding.spinner.setText(data.hint)
            binding.spinner.setOnClickListener {
                itemClickListener.onClickListener(Pair(data,data.hint))
            }
        }

    }


    class EditTextViewHolder(private val binding: EditTextLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.EditText, itemClickListener: OnItemClickListener) {
            binding.ed.hint = data.hint
            binding.edLayout.hint = data.hint
            binding.ed.doOnTextChanged { text, _, _, _ ->
                if (!isInvalidString(text.toString())) {
                    itemClickListener.onClickListener(Pair(data, text))
                }
            }
        }

    }

}