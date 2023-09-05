package com.apogee.geomaster.ui.stake.point

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
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
import com.apogee.geomaster.utils.AudioListener
import com.apogee.geomaster.utils.DrawCircles
import com.apogee.geomaster.utils.DrawLinePoint
import com.apogee.geomaster.utils.EASTING
import com.apogee.geomaster.utils.MapType.*
import com.apogee.geomaster.utils.NOTHING
import com.apogee.geomaster.utils.OnItemClickListener
import com.apogee.geomaster.utils.PointPlot
import com.apogee.geomaster.utils.RADIUS
import com.apogee.geomaster.utils.StakeHelper
import com.apogee.geomaster.utils.angleType
import com.apogee.geomaster.utils.calculateDegree
import com.apogee.geomaster.utils.calculateDistanceBetweenPoints
import com.apogee.geomaster.utils.changeIconDrawable
import com.apogee.geomaster.utils.compassOverlay
import com.apogee.geomaster.utils.convertLatitudeAndLongitude
import com.apogee.geomaster.utils.createLog
import com.apogee.geomaster.utils.estWst
import com.apogee.geomaster.utils.getConvertDecimal
import com.apogee.geomaster.utils.hide
import com.apogee.geomaster.utils.isProperLength
import com.apogee.geomaster.utils.northSouth
import com.apogee.geomaster.utils.scaleOverlay
import com.apogee.geomaster.utils.setUpDialogInfo
import com.apogee.geomaster.utils.show
import com.apogee.geomaster.utils.showMessage
import com.apogee.geomaster.utils.showPlainView
import com.apogee.geomaster.utils.showSatellite
import com.apogee.geomaster.utils.showStreetView
import com.apogee.geomaster.utils.zoomAndAnimateToPoints
import com.apogee.geomaster.utils.zoomToPoint
import com.apogee.geomaster.viewmodel.StakePointViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint
import java.lang.StringBuilder


class StakePointFragment : Fragment(R.layout.stake_point_fragment_layout) {

    private lateinit var binding: StakePointFragmentLayoutBinding

    private lateinit var navStakePointAdaptor: StakePointAdaptor

    private val viewModel: StakePointViewModel by viewModels()

    private var currentLocation: Pair<GeoPoint?, MutableList<IGeoPoint>>? = null

    private var isBottomView: Boolean = false
    private var isTopView: Boolean = false

    private val stakePointPlot by lazy {
        PointPlot { res ->
            currentLocation = Pair(
                GeoPoint(res.latitude, res.longitude), currentLocation?.second ?: mutableListOf()
            )
            binding.mapView.zoomAndAnimateToPoints(
                listOf(
                    LabelledGeoPoint(
                        res.latitude, res.longitude
                    )
                )
            )
            showMessage("Selected ${res.latitude} and ${res.longitude}")
        }
    }

    private val audioListener by lazy {
        AudioListener(requireActivity())
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StakePointFragmentLayoutBinding.bind(view)
        (activity as HomeScreen?)?.hideActionBar()

        binding.cvSliderOption.setOnClickListener {
            if (isTopView) {
                hideDirection()
                binding.infoLayout.show()
                binding.dropIc.rotation = 180f
            } else {
                binding.infoLayout.hide()
                binding.dropIc.rotation = 0f
            }
            isTopView = !isTopView
        }
        binding.cvBottom.setOnClickListener {
            if (isBottomView) {
                hideDirection()
                binding.layoutDrop.show()
                binding.dropUp.rotation = 0f
            } else {
                binding.layoutDrop.hide()
                binding.dropUp.rotation = 180f
            }
            isBottomView = !isBottomView
        }

        binding.bubblePoint.setOnClickListener {
            activity?.setUpDialogInfo { map ->
                when (valueOf(map)) {
                    STATALLITE -> binding.mapView.showSatellite()
                    STEETVIEW -> binding.mapView.showStreetView()
                    PLANEVIEW -> binding.mapView.showPlainView()
                }
            }
        }

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
                                    "Elevation :${getConvertDecimal(data[StakeHelper.ELEVATION] as Double)}"
                                binding.altiuide.changeIconDrawable(R.drawable.ic_elevation_item)
                                binding.altiuide.text =
                                    "Fill :${getConvertDecimal(data[StakeHelper.ELEVATION] as Double / 2)}"
                                binding.tvSigmaX.text = ("σ X : ${data[StakeHelper.XAXIS]}")
                                binding.tvSigmaY.text = ("σ Y : ${data[StakeHelper.YAXIS]}")
                                binding.tvSigmaZ.text = ("σ Z : ${data[StakeHelper.ZAXIS]}")

                                currentLocation?.first?.let { desLocation ->
                                    binding.mapView.overlays.clear()

                                    binding.mapView.overlays.add(
                                        requireActivity().scaleOverlay(
                                            binding.mapView
                                        )
                                    )

                                    binding.mapView.overlays.add(
                                        requireActivity().compassOverlay(
                                            binding.mapView
                                        )
                                    )

                                    val startPoint = GeoPoint(latitude, longitude)
                                    val points: MutableList<IGeoPoint> = ArrayList()
                                    points.addAll(currentLocation?.second ?: emptyList())
                                    points.add(LabelledGeoPoint(latitude, longitude, "Me"))

                                    binding.mapView.overlays.add(
                                        stakePointPlot.plotPointOnMap(
                                            points
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

                                    binding.mapView.overlays.add(
                                        DrawLinePoint(
                                            desLocation, startPoint
                                        )
                                    )

                                    changeColor()
                                    findDistance(startPoint, desLocation)
                                }

                            }
                        }
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun findDistance(desPoint: GeoPoint, startPoint: GeoPoint) {
        val desEstWst = convertLatitudeAndLongitude(desPoint.latitude, desPoint.longitude)
        val startEstWst = convertLatitudeAndLongitude(startPoint.latitude, startPoint.longitude)

        val distance = calculateDistanceBetweenPoints(
            desEstWst.first, desEstWst.second, startEstWst.first, startEstWst.second
        )


        var angle = calculateDegree(
            desEstWst.first, desEstWst.second, startEstWst.first, startEstWst.second
        )
        angle = 360 - angle
        angle += 90
        if (angle >= 360) {
            angle -= 360
        }

        Log.i(
            "TAG_INFO",
            "findDistance: Destination Point X =${desEstWst.first} Y=${desEstWst.second}" + "\n Start Point X= ${startEstWst.first} Y=${startEstWst.second} " + "\n Angle is $angle" + " $distance"
        )

        val sentence = StringBuilder()

        binding.distance.text = "Distance ${isProperLength(distance)}"

        binding.elevationAngle.text = ("${angleType(angle)} Degree")

        estWst((startEstWst.first - desEstWst.first)).let {
            when (EASTING.valueOf(it.second)) {
                EASTING.EAST -> {
                    binding.degreeTxt.changeIconDrawable(R.drawable.ic_easting)
                    binding.degreeTxt.text = "${it.first} East"
                    binding.arrowRightDirection.changeIconDrawable(
                        R.drawable.right_direction_arrow, position = 3
                    )
                    binding.arrowRightDirection.text = it.first
                    if (!showArrow()) binding.arrowRightDirection.show()
                    //speak("Move ${it.first} toward Eastward")
                    sentence.append("Move ${it.first} toward Eastward")
                    sentence.append(" and then ")
                }

                EASTING.WEST -> {
                    binding.degreeTxt.changeIconDrawable(R.drawable.ic_west)
                    binding.degreeTxt.text = "${it.first} West"
                    binding.arrowLeftDirection.changeIconDrawable(
                        R.drawable.left_direction_arrow, position = 1
                    )
                    binding.arrowLeftDirection.text = it.first
                    if (!showArrow()) binding.arrowLeftDirection.show()
                    //speak("Move ${it.first} towards Westwards")
                    sentence.append("Move ${it.first} towards Westwards")
                    sentence.append(" and then ")
                }
            }
        }

        northSouth((startEstWst.second - desEstWst.second)).let {
            when (NOTHING.valueOf(it.second)) {
                NOTHING.NORTH -> {
                    binding.northSouthKey.changeIconDrawable(R.drawable.ic_up_northing)
                    binding.northSouthKey.text = "${it.first} North"
                    binding.arrowTopDirection.changeIconDrawable(
                        R.drawable.up_direction_arrow, position = 2
                    )
                    binding.arrowTopDirection.text = it.first
                    if (!showArrow()) binding.arrowTopDirection.show()
                    sentence.append("Move ${it.first} toward Northward")
                }

                NOTHING.SOUTH -> {
                    binding.northSouthKey.changeIconDrawable(R.drawable.ic_south)
                    binding.northSouthKey.text = "${it.first} South"
                    binding.arrowDownDirection.text = it.first
                    if (!showArrow()) binding.arrowDownDirection.show()
                    binding.arrowDownDirection.changeIconDrawable(
                        R.drawable.down_direction_arrow, position = 4
                    )
                    sentence.append("Move ${it.first} toward Southward")
                }
            }

        }
        binding.mapView.overlays.add(
            DrawCircles(
                desPoint, Paint(Paint.ANTI_ALIAS_FLAG), RADIUS,//meter
                if (distance <= RADIUS) {
                    Color.GREEN
                } else {
                    Color.RED
                }
            )
        )


        if (distance <= 0.2) {
            val boundingBox = BoundingBox.fromGeoPoints(listOf(desPoint, startPoint))
            binding.mapView.zoomToBoundingBox(boundingBox, true)
            binding.mapView.invalidate()
            sentence.clear()
            sentence.append("Arrived to Point")
        } else {
            binding.mapView.controller.setCenter(desPoint)
        }
        speak(sentence.toString())
        /*  binding.directionX.text = " ${estWst((endEstWst.first - startEstWst.first))}"
          binding.directionY.text = " ${northSouth((startEstWst.second - endEstWst.second))}"*/

    }


    private fun getPoints() {
        lifecycleScope.launch {
            delay(3000)
            viewModel.getPoint()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.buffer(Channel.UNLIMITED).collect { res ->
                    res?.let {
                        when (it) {
                            is ApiResponse.Error -> {}
                            is ApiResponse.Loading -> {}
                            is ApiResponse.Success -> {
                                val ls = it.data
                                createLog(
                                    "TAG_INFO",
                                    "Point_LIST IS THERE POINTS ${ls?.second?.size} STOCK ${ls?.first?.size}"
                                )

                                if (ls != null && ls.first.isNotEmpty() && ls.second.isNotEmpty()) {
                                    try {
                                        navStakePointAdaptor.submitList(ls.first)
                                        currentLocation =
                                            Pair(currentLocation?.first, ls.second)
                                        binding.mapView.overlays.add(
                                            stakePointPlot.plotPointOnMap(
                                                ls.second
                                            )
                                        )/*{ res ->
                                                currentLocation = Pair(
                                                    GeoPoint(res.latitude, res.longitude),
                                                    ls.second
                                                )
                                                binding.mapView.zoomAndAnimateToPoints(
                                                    listOf(
                                                        LabelledGeoPoint(
                                                            res.latitude,
                                                            res.longitude
                                                        )
                                                    )
                                                )
                                                showMessage("${res.latitude} and ${res.longitude}")
                                            }*/
                                        binding.mapView.controller.zoomToPoint(
                                            12.5, GeoPoint(
                                                ls.second.first().latitude,
                                                ls.second.first().longitude,
                                                FakeStakePointRepository.altitude
                                            )
                                        )
                                    } catch (e: Exception) {
                                        createLog(
                                            "TAG_INFO", "PLOTTING CRASH ${e.localizedMessage}"
                                        )
                                    }
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
            requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireActivity())
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
                if (response is SurveyModel) {
                    currentLocation = Pair(
                        GeoPoint(response.easting, response.northing),
                        currentLocation?.second ?: mutableListOf()
                    )
                    val obj = LabelledGeoPoint(
                        response.easting, response.northing, response.pointName
                    )
                    //binding.mapView.overlays.add(plotPointOnMap(mutableListOf(obj)))

                    binding.mapView.zoomAndAnimateToPoints(listOf(obj))
                }
            }
        })
        binding.listSlide.adapter = navStakePointAdaptor
        binding.listSlide.addItemDecoration(
            DividerItemDecoration(
                requireActivity(), DividerItemDecoration.VERTICAL
            )
        )

    }

    private fun hideDirection() {
        binding.arrowTopDirection.hide()
        binding.arrowDownDirection.hide()
        binding.arrowLeftDirection.hide()
        binding.arrowRightDirection.hide()
    }

    private fun showArrow() = binding.layoutDrop.isVisible && binding.infoLayout.isVisible

    private fun changeColor() {
        binding.arrowTopDirection.changeIconDrawable(
            R.drawable.up_direction_arrow, R.color.arrow_grey, 2
        )
        binding.arrowTopDirection.text = "0"

        binding.arrowDownDirection.changeIconDrawable(
            R.drawable.down_direction_arrow, R.color.arrow_grey, 4
        )
        binding.arrowDownDirection.text = "0"

        binding.arrowLeftDirection.changeIconDrawable(
            R.drawable.left_direction_arrow, R.color.arrow_grey, 1
        )
        binding.arrowLeftDirection.text = "0"

        binding.arrowRightDirection.changeIconDrawable(
            R.drawable.right_direction_arrow, R.color.arrow_grey, 3
        )
        binding.arrowRightDirection.text = "0"
    }


    private fun speak(data: String) {
        runBlocking {
            async {
                audioListener.speak(data)
            }.await()
        }
    }
}