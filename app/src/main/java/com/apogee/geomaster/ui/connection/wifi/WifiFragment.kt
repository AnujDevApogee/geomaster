package com.apogee.geomaster.ui.connection.wifi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConnectionAdaptor
import com.apogee.geomaster.databinding.WifiConnectionLayoutBinding
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.ui.connection.ConnectionFragmentDirections
import com.apogee.geomaster.utils.OnItemClickListener

class WifiFragment : Fragment(R.layout.wifi_connection_layout) {

    private lateinit var binding: WifiConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<Map<String, Any?>>

    companion object {
        val WifiList = mutableListOf<Map<String, Any?>>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WifiConnectionLayoutBinding.bind(view)
        setupRecycle()
        binding.setCommBtn.setOnClickListener {
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections
                    .actionConnectionFragmentToCreateWifiConnection("Base", "WiFi")
            )
        }
        binding.root
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@WifiFragment.adaptor =
                ConnectionAdaptor(WifiList, object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        if (response is Pair<*, *> && (response.first as Boolean)) {

                        }
                        if (response is Pair<*, *> && !(response.first as Boolean)) {
                            this@WifiFragment.adaptor.notifyDataSetChanged()
                            WifiList.remove(response.second as Map<*, *>)
                        }
                    }
                })
            adapter = adaptor
        }
    }

}