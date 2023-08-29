package com.apogee.geomaster.ui.stake.point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.StakePointFragmentLayoutBinding
import com.apogee.geomaster.ui.HomeScreen

class StakePointFragment : Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()


    }

}