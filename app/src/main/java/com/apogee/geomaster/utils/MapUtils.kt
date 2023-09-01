package com.apogee.geomaster.utils

import android.app.Activity
import android.graphics.Canvas
import org.osmdroid.views.MapView
import org.osmdroid.views.Projection
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay

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