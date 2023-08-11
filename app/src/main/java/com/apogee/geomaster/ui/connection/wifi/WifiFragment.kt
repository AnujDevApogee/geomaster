package com.apogee.geomaster.ui.connection.wifi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConnectionAdaptor
import com.apogee.geomaster.databinding.WifiConnectionLayoutBinding
import com.apogee.geomaster.model.WifiConnection
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.ui.connection.ConnectionFragmentDirections
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.toastMsg

class WifiFragment : Fragment(R.layout.wifi_connection_layout) {

    private lateinit var binding: WifiConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<WifiConnection>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WifiConnectionLayoutBinding.bind(view)
        setupRecycle()
        binding.setCommBtn.setOnClickListener {
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections
                    .actionConnectionFragmentToCreateWifiConnection()
            )
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@WifiFragment.adaptor =
                ConnectionAdaptor(WifiConnection.list, object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        activity?.toastMsg("$response")
                    }
                })
            adapter = adaptor
        }
    }

}