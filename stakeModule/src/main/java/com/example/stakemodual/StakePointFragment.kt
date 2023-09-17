package com.example.stakemodual

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.apogee.geomaster.model.SurveyModel
import com.apogee.geomaster.utils.DrawLinePoint
import com.example.stakemodual.adaptor.StakeMapListAdaptor
import com.example.stakemodual.adaptor.StakePointAdaptor
import com.example.stakemodual.databinding.StakePointFragmentLayoutBinding
import com.example.stakemodual.dialog.setUpDialogInfo
import com.example.stakemodual.model.StakeMapLine
import com.example.stakemodual.repo.StakePointRepository
import com.example.stakemodual.utils.ApiResponse
import com.example.stakemodual.utils.AudioListener
import com.example.stakemodual.utils.DrawCircles
import com.example.stakemodual.utils.EASTING
import com.example.stakemodual.utils.MapType
import com.example.stakemodual.utils.MockStakePointImpl
import com.example.stakemodual.utils.NOTHING
import com.example.stakemodual.utils.OnItemClickListener
import com.example.stakemodual.utils.PointPlot
import com.example.stakemodual.utils.RADIUS
import com.example.stakemodual.utils.StakeHelper
import com.example.stakemodual.utils.angleType
import com.example.stakemodual.utils.calculateDegree
import com.example.stakemodual.utils.calculateDistanceBetweenPoints
import com.example.stakemodual.utils.changeIconDrawable
import com.example.stakemodual.utils.compassOverlay
import com.example.stakemodual.utils.convertLatitudeAndLongitude
import com.example.stakemodual.utils.createLog
import com.example.stakemodual.utils.estWst
import com.example.stakemodual.utils.getConvertDecimal
import com.example.stakemodual.utils.hide
import com.example.stakemodual.utils.isProperLength
import com.example.stakemodual.utils.northSouth
import com.example.stakemodual.utils.scaleOverlay
import com.example.stakemodual.utils.setHtmlTxt
import com.example.stakemodual.utils.show
import com.example.stakemodual.utils.showMessage
import com.example.stakemodual.utils.showPlainView
import com.example.stakemodual.utils.showSatellite
import com.example.stakemodual.utils.showStreetView
import com.example.stakemodual.utils.zoomAndAnimateToPoints
import com.example.stakemodual.utils.zoomToPoint
import com.example.stakemodual.viewmodel.StakePointViewModel
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


class StakePointFragment : Fragment(), MockStakePointImpl {

    private lateinit var binding: StakePointFragmentLayoutBinding

    private lateinit var navStakePointAdaptor: StakePointAdaptor

    private val viewModel by lazy {
        StakePointRepository()
    }

    private var currentLocation: Pair<GeoPoint?, MutableList<IGeoPoint>>? = null

    private var isBottomView: Boolean = false
    private var isTopView: Boolean = false
    private val sentence = StringBuilder()
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

    private val recycleViewCallBack = object : OnItemClickListener {
        override fun <T> onClickListener(response: T) {
            if (response is StakeMapLine && response.id == R.drawable.ic_setting) {
                activity?.setUpDialogInfo { map ->
                    when (MapType.valueOf(map)) {
                        MapType.STATALLITE -> binding.mapView.showSatellite()
                        MapType.STEETVIEW -> binding.mapView.showStreetView()
                        MapType.PLANEVIEW -> binding.mapView.showPlainView()
                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StakePointFragmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                //binding.layoutDrop.show()
                binding.bottomRecycleViews.show()
                binding.topRecycleViews.show()
                binding.dropUp.rotation = 0f
            } else {
                binding.bottomRecycleViews.hide()
                binding.topRecycleViews.hide()
                binding.dropUp.rotation = 180f
            }
            isBottomView = !isBottomView
        }
        setAdaptor()
        setupMap()
        setUpAdaptor()
        getPoints()
        getCoordinate()


    }

    private fun setAdaptor() {
        binding.topRecycleViews.apply {
            val adapter = StakeMapListAdaptor(recycleViewCallBack)
            this.adapter = adapter
            adapter.submitList(StakeMapLine.list)
        }
        binding.bottomRecycleViews.apply {
            val adapter = StakeMapListAdaptor(recycleViewCallBack)
            this.adapter = adapter
            adapter.submitList(StakeMapLine.otherLayout)
        }
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

                                binding.northIngTxt.text = " "
                                binding.northIngTxt.append(setHtmlTxt("N", "'#B5B5B5'"))
                                binding.northIngTxt.append("\t")
                                binding.northIngTxt.append("$latitude")

                                binding.eastingTxt.text = " "
                                binding.eastingTxt.append(setHtmlTxt("E", "'#B5B5B5'"))
                                binding.eastingTxt.append("\t\t$longitude")

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


        sentence.clear()
        binding.distance.text = " "
        binding.distance.append(setHtmlTxt("Distance", "'#B5B5B5'"))
        binding.distance.append("\t${isProperLength(distance)}")

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
                startPoint, Paint(Paint.ANTI_ALIAS_FLAG), RADIUS,//meter
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.buffer(Channel.UNLIMITED).collect { res ->
                    res?.let {
                        when (it) {
                            is ApiResponse.Error -> {}
                            is ApiResponse.Loading -> {
                                showPb()
                            }

                            is ApiResponse.Success -> {
                                val ls = it.data
                                createLog(
                                    "TAG_INFO",
                                    "Point_LIST IS THERE POINTS ${ls?.second?.size} STOCK ${ls?.first?.size}"
                                )

                                if (ls != null && ls.first.isNotEmpty() && ls.second.isNotEmpty()) {
                                    try {
                                        hidePb()
                                        binding.mapView.overlays.clear()
                                        binding.mapView.overlays.add(
                                            requireActivity().compassOverlay(
                                                binding.mapView
                                            )
                                        )
                                        binding.mapView.overlays.add(
                                            requireActivity().scaleOverlay(
                                                binding.mapView
                                            )
                                        )
                                        navStakePointAdaptor.submitList(ls.first)
                                        currentLocation =
                                            Pair(currentLocation?.first, ls.second)

                                        binding.mapView.overlays.add(
                                            stakePointPlot.plotPointOnMap(
                                                ls.second
                                            )
                                        )
                                        binding.mapView.controller.zoomToPoint(
                                            12.5, GeoPoint(
                                                ls.second.first().latitude,
                                                ls.second.first().longitude
                                            )
                                        )
                                        binding.mapView.invalidate()
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

    private fun showArrow() =
        binding.topRecycleViews.isVisible && binding.infoLayout.isVisible && binding.bottomRecycleViews.isVisible

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

    private fun showPb() {
        binding.pb.isVisible = true
    }

    private fun hidePb() {
        binding.pb.isVisible = false
    }

    override fun receivePoint(surveyModel: SurveyModel) {
        viewModel.receivePoint(surveyModel)
    }

    override fun stakePoint(hashMap: HashMap<String, Any>) {
        viewModel.stakePoint(hashMap)
    }
}