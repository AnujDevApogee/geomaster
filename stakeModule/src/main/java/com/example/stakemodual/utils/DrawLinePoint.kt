package com.apogee.geomaster.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay



class DrawLinePoint(
    private val start: GeoPoint,
    private val end: GeoPoint,

) : Overlay() {

    override fun draw(canvas: Canvas, map: MapView, shadow: Boolean) {
        val screenStartPoint = map.projection.toPixels(start, null)
        val screenEndPoint = map.projection.toPixels(end, null)
        val drawPaint = Paint()
        drawPaint.color = Color.BLUE
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 2f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)

        val path = Path()
        path.moveTo(screenStartPoint.x.toFloat(), screenStartPoint.y.toFloat())
        path.lineTo(screenEndPoint.x.toFloat(), screenEndPoint.y.toFloat())
        path.close()

        canvas.drawPath(path,drawPaint)
    }

}