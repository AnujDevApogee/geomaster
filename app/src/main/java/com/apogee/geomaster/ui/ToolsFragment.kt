package com.apogee.geomaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.ToolsFragmentLayoutBinding


class ToolsFragment : Fragment(R.layout.tools_fragment_layout) {

    private lateinit var binding: ToolsFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= ToolsFragmentLayoutBinding.bind(view)

    }
}