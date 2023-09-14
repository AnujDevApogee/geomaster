package com.apogee.geomaster.adaptor.viewholder

import android.R
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.apogee.geomaster.databinding.EditTextLayoutBinding
import com.apogee.geomaster.databinding.SpinnerDropdownLayoutBinding
import com.apogee.geomaster.model.DynamicViewType
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.createLog

sealed class MultiViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {


    class DropDownViewHolder(private val binding: SpinnerDropdownLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.SpinnerData, itemClickListener: OnItemClickListener) {
            binding.spinnerTextInputLayout.hint = data.hint
            binding.spinner.setText(data.hint)
            val coordinateAdaptor: ArrayAdapter<String> = ArrayAdapter<String>(
                binding.spinner.context,
                R.layout.select_dialog_item,
                data.dataList
            )
            if (data.dataList.isNotEmpty()) {
                binding.spinner.setText(data.dataList.first())
                val selectedItem = Pair(data.dataList.first(), data.valueList.first())
                data.selectedPair = selectedItem
                itemClickListener.onClickListener(data)
            }
            binding.spinner.setAdapter(coordinateAdaptor)
            binding.spinner.setOnItemClickListener { _, _, position, _ ->
                createLog("TAG_SPINNER", "Position $position")
                val selectedItem = Pair(data.dataList[position], data.valueList[position])
                data.selectedPair = selectedItem
                itemClickListener.onClickListener(data)
            }
            /*  binding.spinner.setOnClickListener {
                  itemClickListener.onClickListener(Pair(data,data.hint))
              }*/
        }

    }


    class EditTextViewHolder(private val binding: EditTextLayoutBinding) :
        MultiViewHolder(binding) {

        fun bindIt(data: DynamicViewType.EditText, itemClickListener: OnItemClickListener) {
            binding.ed.hint = data.hint
            binding.edLayout.hint = data.hint
            binding.ed.doOnTextChanged { text, _, _, _ ->
                createLog("TAG_RESPONSE", "Option is -> $text  ${text.isNullOrEmpty()}")
                data.data = text?.toString()
                itemClickListener.onClickListener(data)
            }
        }

    }

}