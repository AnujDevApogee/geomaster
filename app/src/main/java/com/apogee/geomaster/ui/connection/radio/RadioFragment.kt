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
import com.apogee.geomaster.ui.connection.internet.InternetFragment
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.utils.toastMsg
import com.apogee.updatedblelibrary.Utils.checkString

class RadioFragment : Fragment(R.layout.radio_connection_layout) {

    private lateinit var binding: RadioConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<Map<String, Any?>>


    companion object {
        val Radio = mutableListOf<Map<String, Any?>>()
    }
    private var mode = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioConnectionLayoutBinding.bind(view)
        setupRecycle()


        binding.setCommBtn.setOnClickListener {
            if (binding.externalRadioBtn.isChecked) {
                mode = "RS232"
            }
            if (binding.internalRadioBtn.isChecked) {
                mode = "Radio"
            }
            if (checkString(mode)) {
                showMessage("Please select the Mode!!")
                return@setOnClickListener
            }
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections.actionConnectionFragmentToCreateRadioConnectionFragment(
                    "BASE", mode
                )
            )
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@RadioFragment.adaptor =
                ConnectionAdaptor(Radio, object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        if (response is Pair<*, *> && (response.first as Boolean)) {
                            (parentFragment as ConnectionFragment).selectTheSetting(
                                mode,
                                response.second as Map<String, Any?>
                            )
                        }
                        if (response is Pair<*, *> && !(response.first as Boolean)){
                            this@RadioFragment.adaptor.notifyDataSetChanged()
                            Radio.remove(response.second as Map<*, *>)
                        }
                    }
                })
            adapter = adaptor
        }
    }

}