package com.apogee.geomaster.ui.connection.internet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConnectionAdaptor
import com.apogee.geomaster.databinding.InternetConnectionLayoutBinding
import com.apogee.geomaster.model.NetworkConnection
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.toastMsg

class InternetFragment : Fragment(R.layout.internet_connection_layout) {

    private lateinit var binding: InternetConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<NetworkConnection>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InternetConnectionLayoutBinding.bind(view)
        setupRecycle()
        binding.setCommBtn.setOnClickListener {
            (parentFragment as ConnectionFragment).goToCreateConnection()
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@InternetFragment.adaptor =
                ConnectionAdaptor(NetworkConnection.list, object : OnItemClickListener {
                    override fun <T> onClickListener(response: T) {
                        activity?.toastMsg("$response")
                    }
                })
            adapter=adaptor
        }
    }
}