package com.apogee.geomaster.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.SurveyFragmentLayoutBinding

class SurveyFragment : Fragment(R.layout.survey_fragment_layout) {
    private lateinit var binding: SurveyFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SurveyFragmentLayoutBinding.bind(view)

    }
}