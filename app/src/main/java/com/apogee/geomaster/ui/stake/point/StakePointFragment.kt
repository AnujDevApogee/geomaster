package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.apogee.geomaster.R
import com.apogee.geomaster.databinding.StakePointFragmentLayoutBinding
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnSwipeTouchListener
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.showMessage

class StakePointFragment: Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding


    private val list by lazy {
        listOf("Point 1", "Point 2", "Point 3", "Point 4", "Point 5", "Point 6", "Point 7")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()

        binding.cvSliderOption.setOnTouchListener(swipeGesture(binding.infoLayout, false))
        binding.cvBottom.setOnTouchListener(swipeGesture(binding.layoutDrop, true))


        setUpAdaptor()

        binding.listSlidermenu.setOnItemClickListener { _, _, position, id ->
            showMessage("${binding.listSlidermenu.getItemAtPosition(position)}")

        }

    }

    private fun setUpAdaptor() {
        binding.listSlidermenu.adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list)
    }


    private fun swipeGesture(view: View, isBottomView: Boolean): OnSwipeTouchListener {
        return object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                createLog("TOUCH_MSG", "Bottom RIGHT")
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                createLog("TOUCH_MSG", "Bottom LEFT")
            }

            override fun onSwipeTop() {
                super.onSwipeTop()
                if (!isBottomView)
                view.hide()
                else
                    view.show()
                createLog("TOUCH_MSG","Bottom TOP")
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                if (!isBottomView)
                view.show()
                else
                    view.hide()
                createLog("TOUCH_MSG","Bottom click")
            }
        }
    }

}