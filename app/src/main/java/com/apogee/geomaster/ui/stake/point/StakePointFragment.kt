package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
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
import com.apogee.geomaster.utils.DrawCircles
import com.apogee.geomaster.utils.DrawLinePoint
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.OnSwipeTouchListener
import com.apogee.geomaster.utils.RADIUS
import com.apogee.geomaster.utils.StakeHelper
import com.apogee.geomaster.utils.compassOverlay
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.plotPointOnMap
import com.apogee.geomaster.utils.scaleOverlay
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.zoomAndAnimateToPoints
import com.apogee.geomaster.utils.zoomToPoint
import com.apogee.geomaster.viewmodel.StakePointViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint


class StakePointFragment: Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding

    private lateinit var navStakePointAdaptor: StakePointAdaptor

    private val viewModel: StakePointViewModel by viewModels()

    private var currentLocation: GeoPoint? = null
    private var isUpdateMap: Boolean = false

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
        getCoordinate()

    }

    @SuppressLint("SetTextI18n")
    private fun getCoordinate() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentData.collect { res ->
                    res?.let {
                        when (it) {
                            is ApiResponse.Error -> {}
                            is ApiResponse.Loading -> {}
                            is ApiResponse.Success -> {
                                val data = it.data!!
                                val latitude = data[StakeHelper.LATITUDE] as Double
                                val longitude = data[StakeHelper.LONGITUDE] as Double

                                binding.northIngTxt.text = "N :${latitude}"
                                binding.eastingTxt.text = "E :${longitude}"
                                binding.elevationKy.text =
                                    "Elevation :${data[StakeHelper.ELEVATION]}"
                                binding.altiuide.text = "Altitude :${data[StakeHelper.ELEVATION]}"
                                binding.tvSigmaX.text = ("σ X : ${data[StakeHelper.XAXIS]}")
                                binding.tvSigmaY.text = ("σ Y : ${data[StakeHelper.YAXIS]}")
                                binding.tvSigmaZ.text = ("σ Z : ${data[StakeHelper.ZAXIS]}")

                                currentLocation?.let { desLocation ->
                                    if (isUpdateMap) {
                                        binding.mapView.overlays.removeAt(binding.mapView.overlays.size - 2)
                                        binding.mapView.overlays.removeAt(binding.mapView.overlays.size - 3)
                                        binding.mapView.overlays.removeAt(binding.mapView.overlays.size - 4)
                                    } else {
                                        isUpdateMap = true
                                    }
                                    val startPoint = GeoPoint(latitude, longitude)
                                    val points: MutableList<IGeoPoint> = ArrayList()
                                    points.add(LabelledGeoPoint(latitude, longitude, "Me"))
                                    binding.mapView.overlays.add(plotPointOnMap(points))
                                    binding.mapView.overlays.add(
                                        DrawLinePoint(
                                            desLocation,
                                            startPoint
                                        )
                                    )
                                    binding.mapView.overlays.add(
                                        DrawCircles(
                                            desLocation,
                                            Paint(Paint.ANTI_ALIAS_FLAG),
                                            RADIUS,//meter
                                            Color.RED
                                        )
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
                                    createLog(
                                        "TAG_INFO",
                                        "LIST IS THERE POINTS ${ls?.second?.size} STOCK ${ls?.first?.size}"
                                    )

                                    if (ls != null && ls.first.isNotEmpty() && ls.second.isNotEmpty()) {
                                        navStakePointAdaptor.submitList(ls.first)
                                        binding.mapView.overlays.add(plotPointOnMap(ls.second))
                                        binding.mapView.controller.zoomToPoint(
                                            12.5,
                                            GeoPoint(
                                                ls.second.first().latitude,
                                                ls.second.first().longitude,
                                                FakeStakePointRepository.altitude
                                            )
                                        )
                                    }

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
                    currentLocation = GeoPoint(response.easting, response.northing)
                    val obj = LabelledGeoPoint(
                        response.easting,
                        response.northing,
                        response.pointName
                    )
                    //binding.mapView.overlays.add(plotPointOnMap(mutableListOf(obj)))

                    binding.mapView.zoomAndAnimateToPoints(listOf(obj))
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