package com.apogee.geomaster.ui.connection.radio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.RadioConnectionLayoutBinding

class RadioFragment : Fragment(R.layout.radio_connection_layout) {

    private lateinit var binding: RadioConnectionLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= RadioConnectionLayoutBinding.bind(view)

    }

}