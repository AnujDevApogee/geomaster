package com.example.stakemodual.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay


class DrawCircles(
    private val geoPoint: GeoPoint,
    private val radius: Double,
    private val color: Int=Color.RED
) :
    Overlay() {
    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        val screenPoint = mapView.projection.toPixels(geoPoint, null)
        val circleRadius = mapView.projection.metersToPixels(radius.toFloat())
        val centerX=screenPoint.x
        val centerY=screenPoint.y

        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        val crossPaint = Paint()
        crossPaint.color = color
        crossPaint.strokeWidth = 5f

        // Draw the vertical line of the cross
        val startX2 = centerX.toFloat()
        val startY2 = (centerY - circleRadius)
        val endX2 = centerX.toFloat()
        val endY2 = (centerY + circleRadius)

        // Draw the Horizontal line of the cross
        val startX1 = (centerX - circleRadius)
        val startY1 = centerY.toFloat()
        val endX1 = (centerX + circleRadius)
        val endY1 = centerY.toFloat()

        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius, paint)
        canvas.drawLine(startX1, startY1, endX1, endY1, crossPaint)
        canvas.drawLine(startX2, startY2, endX2, endY2, crossPaint)



    }
}