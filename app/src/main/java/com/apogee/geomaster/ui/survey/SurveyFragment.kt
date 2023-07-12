package com.apogee.geomaster.ui.survey

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.HomeScreenAdaptor
import com.apogee.geomaster.databinding.SurveyFragmentLayoutBinding
import com.apogee.geomaster.model.HomeScreenOption
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener

class SurveyFragment : Fragment(R.layout.survey_fragment_layout), OnItemClickListener {
    private lateinit var binding: SurveyFragmentLayoutBinding
    private lateinit var homeScreenAdaptor: HomeScreenAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as  HomeScreen?)?.showActionBar()
        binding = SurveyFragmentLayoutBinding.bind(view)
        recycleView()
        homeScreenAdaptor.submitList(HomeScreenOption.list)
    }
    private fun recycleView() {
        binding.surveyRecycleView.apply {
            homeScreenAdaptor = HomeScreenAdaptor(this@SurveyFragment)
            adapter = homeScreenAdaptor
        }
    }

    override fun <T> onClickListener(response: T) {

    }
}