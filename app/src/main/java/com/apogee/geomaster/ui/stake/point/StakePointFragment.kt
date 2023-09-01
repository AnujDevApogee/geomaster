package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.StakePointAdaptor
import com.apogee.geomaster.databinding.StakePointFragmentLayoutBinding
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.OnSwipeTouchListener
import com.apogee.geomaster.utils.compassOverlay
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.scaleOverlay
import com.apogee.geomaster.utils.show
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController


class StakePointFragment: Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding

    private lateinit var navStakePointAdaptor: StakePointAdaptor

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()

        binding.cvSliderOption.setOnTouchListener(swipeGesture(binding.infoLayout, false))
        binding.cvBottom.setOnTouchListener(swipeGesture(binding.layoutDrop, true))

        setupMap()
        setUpAdaptor()


    }

    private fun setupMap() {
        Configuration.getInstance().load(
            requireActivity(),
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        )
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.overlayManager.tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.overlays.add(requireActivity().scaleOverlay(binding.mapView))
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.overlays.add(requireActivity().compassOverlay(binding.mapView))
    }

    private fun setUpAdaptor() {
        navStakePointAdaptor = StakePointAdaptor(object : OnItemClickListener {
            override fun <T> onClickListener(response: T) {
                binding.drawerPoint.close()
            }
        })
        binding.listSlide.adapter = navStakePointAdaptor
        binding.listSlide.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )
        navStakePointAdaptor.submitList(SurveyModel.list)
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