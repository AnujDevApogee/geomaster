package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.StakePointFragmentLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnSwipeTouchListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.show

class StakePointFragment: Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()

        binding.cvSliderOption.setOnTouchListener(swipeGesture(binding.infoLayout))


    }


    private fun swipeGesture(view: View): OnSwipeTouchListener {
        return object: OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                createLog("TOUCH_MSG","Bottom RIGHT")
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                createLog("TOUCH_MSG","Bottom LEFT")
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                view.hide()
                createLog("TOUCH_MSG","Bottom TOP")
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                view.show()
                createLog("TOUCH_MSG","Bottom click")
            }
        }
    }

}