package com.apogee.geomaster.ui.connection.internet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.ConnectionAdaptor
import com.apogee.geomaster.databinding.InternetConnectionLayoutBinding
import com.apogee.geomaster.ui.connection.ConnectionFragment
import com.apogee.geomaster.ui.connection.ConnectionFragmentDirections
import com.apogee.geomaster.utils.OnItemClickListener

class InternetFragment : Fragment(R.layout.internet_connection_layout) {

    private lateinit var binding: InternetConnectionLayoutBinding
    private lateinit var adaptor: ConnectionAdaptor<Map<String, Any?>>


    companion object {
        val internetWifi = mutableListOf<Map<String, Any?>>()
    }

    private var mode = "GSM"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InternetConnectionLayoutBinding.bind(view)
        setupRecycle()
        binding.setCommBtn.setOnClickListener {
            (parentFragment as ConnectionFragment).goToNxtScr(
                ConnectionFragmentDirections
                    .actionConnectionFragmentToCreateConnectionFragment(mode, "Base")
            )
        }
    }

    private fun setupRecycle() {
        binding.recycleViewLs.apply {
            this@InternetFragment.adaptor =
                ConnectionAdaptor(internetWifi, object : OnItemClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    @Suppress("UNCHECKED_CAST")
                    override fun <T> onClickListener(response: T) {
                        if (response is Pair<*, *> && (response.first as Boolean)) {
                            (parentFragment as ConnectionFragment).selectTheSetting(
                                mode,
                                response.second as Map<String, Any?>
                            )
                        }
                        if (response is Pair<*, *> && !(response.first as Boolean)) {
                            this@InternetFragment.adaptor.notifyDataSetChanged()
                            internetWifi.remove(response.second as Map<*, *>)
                        }
                    }
                })
            adapter = adaptor
        }
    }
}