package com.apogee.geomaster.ui.connection.radio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConnectionAdaptor
import com.apogee.geomaster.databinding.RadioConnectionLayoutBinding
import com.apogee.geomaster.model.RadioConnection
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.ui.connection.ConnectionFragmentDirections
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.utils.toastMsg
import com.apogee.updatedblelibrary.Utils.checkString

class RadioFragment : Fragment(R.layout.radio_connection_layout) {

    private lateinit var binding: RadioConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<RadioConnection>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioConnectionLayoutBinding.bind(view)
        setupRecycle()


        binding.setCommBtn.setOnClickListener {
            var mode = ""
            if (binding.externalRadioBtn.isSelected) {
                mode = "RS232"
            }
            if (binding.internalRadioBtn.isSelected) {
                mode = "Radio"
            }
            if (checkString(mode)) {
                showMessage("Please select the Mode!!")
                return@setOnClickListener
            }
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections.actionConnectionFragmentToCreateRadioConnectionFragment(
                    "BASE", "Radio"
                )
            )
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@RadioFragment.adaptor =
                ConnectionAdaptor(listOf(), object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        activity?.toastMsg("$response")
                    }
                })
            adapter = adaptor
        }
    }

}