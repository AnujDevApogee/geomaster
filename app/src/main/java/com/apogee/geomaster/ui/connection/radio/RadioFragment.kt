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
import com.apogee.geomaster.utils.toastMsg

class RadioFragment : Fragment(R.layout.radio_connection_layout) {

    private lateinit var binding: RadioConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<RadioConnection>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RadioConnectionLayoutBinding.bind(view)
        setupRecycle()

        binding.setCommBtn.setOnClickListener {
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections.actionConnectionFragmentToCreateRadioConnectionFragment()
            )
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@RadioFragment.adaptor =
                ConnectionAdaptor(RadioConnection.list, object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        activity?.toastMsg("$response")
                    }
                })
            adapter = adaptor
        }
    }

}