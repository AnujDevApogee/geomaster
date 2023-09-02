package com.apogee.geomaster.utils

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.api.IGeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
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

    return SimpleFastPointOverlay(pt, opt)
}


fun IMapController.zoomToPoint(zoom: Double, geoPoint: GeoPoint) {
    setCenter(geoPoint)
    animateTo(geoPoint,zoom,2)
}