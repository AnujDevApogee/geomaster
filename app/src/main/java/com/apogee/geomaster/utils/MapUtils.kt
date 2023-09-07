package com.apogee.geomaster.utils

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.api.IGeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme

fun Activity.scaleOverlay(view: MapView): ScaleBarOverlay {
    val metrics = resources.displayMetrics
    val mScaleBar = ScaleBarOverlay(view)
    mScaleBar.setCentred(true)
    mScaleBar.setScaleBarOffset(metrics.widthPixels / 2, 10)
    return mScaleBar
}


fun Activity.compassOverlay(view: MapView) = object : CompassOverlay(this, view) {
    override fun draw(c: Canvas?, pProjection: Projection?) {
        drawCompass(c, -view.mapOrientation, pProjection?.screenRect)
    }
}

class PointPlot(private val pointPlot: (IGeoPoint) -> Unit) {
companion object{
    const val spaceCoordinate="\t\t\t\t\t"
}
    fun plotPointOnMap(points: MutableList<IGeoPoint>): SimpleFastPointOverlay {
        val pt = SimplePointTheme(points, true)
        val textStyle = Paint()
        textStyle.style = Paint.Style.FILL
        textStyle.color = Color.parseColor("#0000ff")
        textStyle.textAlign = Paint.Align.CENTER
        textStyle.textSize = 24f

        val opt = SimpleFastPointOverlayOptions.getDefaultStyle()
            .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
            .setRadius(7f).setIsClickable(true).setCellSize(15)
            .setTextStyle(textStyle).setSymbol(SimpleFastPointOverlayOptions.Shape.CIRCLE)

        SimpleFastPointOverlay(pt, opt).apply {
            setOnClickListener { points, point ->
                pointPlot.invoke(points.get(point))
            }
            return this
        }

    }
}

fun IMapController.zoomToPoint(zoom: Double, geoPoint: GeoPoint) {
    setCenter(geoPoint)
    animateTo(geoPoint, zoom, 2)
}


fun MapView.zoomAndAnimateToPoints(ls: List<GeoPoint>) {
    val boundingBox = BoundingBox.fromGeoPoints(ls)
    zoomToBoundingBox(boundingBox, true)
    invalidate()
}

fun MapView.showStreetView() {
    setTileSource(TileSourceFactory.MAPNIK)
}

fun MapView.showSatellite() {
    setTileSource(TileSourceFactory.USGS_SAT)
}

fun MapView.showPlainView() {
    setTileSource(TileSourceFactory.BASE_OVERLAY_NL)
    overlayManager.tilesOverlay.loadingBackgroundColor = Color.TRANSPARENT
}

enum class MapType {
    STATALLITE,
    STEETVIEW,
    PLANEVIEW
}