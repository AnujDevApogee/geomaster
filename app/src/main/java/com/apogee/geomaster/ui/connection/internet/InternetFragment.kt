package com.apogee.geomaster.ui.connection.internet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.InternetConnectionLayoutBinding

class InternetFragment : Fragment(R.layout.internet_connection_layout) {

    private lateinit var binding: InternetConnectionLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = InternetConnectionLayoutBinding.bind(view)

    }
}