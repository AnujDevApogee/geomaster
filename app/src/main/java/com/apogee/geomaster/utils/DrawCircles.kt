package com.apogee.geomaster.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class DrawCircles(
    private val geoPoint: GeoPoint,
    private var paint: Paint,
    private val radius: Double,
    private val color: Int=Color.RED
) :
    Overlay() {
    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        val screenPoint = mapView.projection.toPixels(geoPoint, null)
        paint = Paint()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        val circleRadius = mapView.projection.metersToPixels(radius.toFloat())
        canvas.drawCircle(screenPoint.x.toFloat(), screenPoint.y.toFloat(), circleRadius, paint)
    }
}