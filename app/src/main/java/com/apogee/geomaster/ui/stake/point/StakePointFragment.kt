package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.apogee.geomaster.R
import com.apogee.geomaster.adaptor.StakePointAdaptor
import com.apogee.geomaster.databinding.StakePointFragmentLayoutBinding
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.repository.FakeStakePointRepository
import com.apogee.geomaster.ui.HomeScreen
import com.apogee.geomaster.utils.ApiResponse
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.OnSwipeTouchListener
import com.apogee.geomaster.utils.compassOverlay
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.plotPointOnMap
import com.apogee.geomaster.utils.scaleOverlay
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.zoomToPoint
import com.apogee.geomaster.viewmodel.StakePointViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint


class StakePointFragment: Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding

    private lateinit var navStakePointAdaptor: StakePointAdaptor

    private val viewModel: StakePointViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()

        binding.cvSliderOption.setOnTouchListener(swipeGesture(binding.infoLayout, false))
        binding.cvBottom.setOnTouchListener(swipeGesture(binding.layoutDrop, true))
        setupMap()
        setUpAdaptor()
        getPoints()
        getGeoPoint()
    }

    private fun getGeoPoint() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pointData
                    .buffer(Channel.UNLIMITED)
                    .collect { res ->
                        res?.let {
                            when (it) {
                                is ApiResponse.Error -> {}
                                is ApiResponse.Loading -> {}
                                is ApiResponse.Success -> {
                                    val ls = it.data
                                    createLog("TAG_INFO", "LIST IS THERE ${ls?.size}")
                                    if (!ls.isNullOrEmpty()) {
                                        binding.mapView.overlays.add(plotPointOnMap(ls))
                                        binding.mapView.controller.zoomToPoint(
                                            12.5,
                                            GeoPoint(ls.first().latitude, ls.first().longitude,FakeStakePointRepository.altitude)
                                        )
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getPoints() {
        lifecycleScope.launch {
            delay(3000)
            viewModel.getPoint()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data
                    .buffer(Channel.UNLIMITED)
                    .collect { res ->
                        res?.let {
                            when (it) {
                                is ApiResponse.Error -> {}
                                is ApiResponse.Loading -> {}
                                is ApiResponse.Success -> {
                                    val ls = it.data
                                    navStakePointAdaptor.submitList(ls)

                                }
                            }
                        }
                    }
            }
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(
            requireActivity(),
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        )
        binding.mapView.setTileSource(TileSourceFactory.BASE_OVERLAY_NL)
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
                if (response is SurveyModel) {
                    val obj = LabelledGeoPoint(
                        response.easting,
                        response.northing,
                        response.pointName
                    )
                    binding.mapView.overlays.add(plotPointOnMap(mutableListOf(obj)))
                    binding.mapView.controller.setCenter(obj)
                }
            }
        })
        binding.listSlide.adapter = navStakePointAdaptor
        binding.listSlide.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                DividerItemDecoration.VERTICAL
            )
        )

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