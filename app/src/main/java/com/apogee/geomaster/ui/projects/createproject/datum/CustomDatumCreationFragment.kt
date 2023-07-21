package com.apogee.geomaster.ui.projects.createproject.datum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.FragmentCustomDatumCreationBinding


class CustomDatumCreationFragment : Fragment(R.layout.fragment_custom_datum_creation) {
    private lateinit var binding: FragmentCustomDatumCreationBinding
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentCustomDatumCreationBinding.bind(view)
    }
}