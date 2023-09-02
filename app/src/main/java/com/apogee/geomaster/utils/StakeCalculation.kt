package com.apogee.geomaster.utils

import android.util.Log
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt


/*Distance calculation*/
fun calculateDistanceBetweenPoints(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    return sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))
}


fun convertLatitudeAndLongitude(latitude: Double, longitude: Double): Pair<Double, Double> {
    val equatorialRadius = 6378137.0
    val flattening = 298.2572235630
    val a = equatorialRadius
    val f = 1 / flattening
    val b: Double = a * (1 - f)
    val e = sqrt(1 - b.pow(2.0) / a.pow(2.0))
    val e0sq = e * e / (1 - e.pow(2.0))
    val esq = 1 - b / a * (b / a)
    val latRad = latitude * Math.PI / 180.0
    // double latRad = latitude * 3.1428571429 / 180.0;
    val utmz = 1 + Math.floor((longitude + 180) / 6) // utm zone
    val zcm = 3 + 6 * (utmz - 1) - 180 // central meridian of a zone
    var latz = 0.0 // zone A-B for below 80S
    // convert latitude to latitude zone
    if (latitude > -80 && latitude < 72) {
        latz = Math.floor((latitude + 80) / 8) + 2 // zones C-W
    } else {
        if (latitude > 72 && latitude < 84) {
            latz = 21.0 // zone X
        } else {
            if (latitude > 84) {
                latz = 23.0 // zones Y-Z
            }
        }
    }
    val N: Double = a / sqrt(1 - (e * sin(latRad)).pow(2.0))
    val T = Math.pow(Math.tan(latRad), 2.0)
    val C: Double = e0sq * cos(latRad).pow(2.0)
    val A = (longitude - zcm) * Math.PI / 180.0 * Math.cos(latRad)

    // calculate M (USGS style)
    var M: Double = latRad * (1.0 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5.0 * esq / 256.0)))
    M -= sin(2.0 * latRad) * (esq * (3.0 / 8.0 + esq * (3.0 / 32.0 + 45.0 * esq / 1024.0)))
    M += sin(4.0 * latRad) * (esq * esq * (15.0 / 256.0 + esq * 45.0 / 1024.0))
    M -= sin(6.0 * latRad) * (esq * esq * esq * (35.0 / 3072.0))
    M *= a //Arc length along standard meridian

    // calculate easting
    val k0 = 0.9996
    var x: Double =
        k0 * N * A * (1.0 + A * A * ((1.0 - T + C) / 6.0 + A * A * (5.0 - 18.0 * T + T * T + 72.0 * C - 58.0 * e0sq) / 120.0)) //Easting relative to CM
    x = x + 500000 // standard easting

    // calculate northing
    var y: Double =
        k0 * (M + N * Math.tan(latRad) * (A * A * (1.0 / 2.0 + A * A * ((5.0 - T + 9.0 * C + 4.0 * C * C) / 24.0 + A * A * (61.0 - 58.0 * T + T * T + 600.0 * C - 330.0 * e0sq) / 720.0)))) // from the equator
    if (y < 0) {
        y = 10000000 + y // add in false northing if south of the equator
    }
//    longitudeZoneValue = utmz.toInt()
//    latitudeZoneValue = latz.toInt()
//    eastingValue = x
//    northingValue = y


    return Pair(x,y)
}



fun calculateDegree(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    System.err.println("var fix - $x1 - $y1 - $x2 - $y2")
    var angle = 0.00
    try {
        angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1))
        //angle = Math.toDegrees(Math.atan2( 1441506.909 - 1441523.856,325863.269 - 326103.627))

        Log.d("TAG", "getAngleIn360: " + angle)
        // Keep angle between 0 and 360
        if (angle < 0) {
            angle = angle + Math.ceil(-angle / 360) * 360
        }

    } catch (e: Exception) {
        println("Model.WebServiceModel.getAngleIn360()- $e")
    }
    return angle
}

const val RADIUS = 0.02


enum class EASTING {
    EAST,
    WEST
}

enum class NOTHING {
    NORTH,
    SOUTH
}


fun northSouth(d: Double): Pair<String, String> {
    if (sign(d) == -1.0) {
        return Pair(
            isProperLength(abs(d)),
            NOTHING.SOUTH.name
        )// + " South ${getEmojiByUnicode(	0x2B07)}"
    }
    return Pair(isProperLength(abs(d)), NOTHING.NORTH.name)
}

fun estWst(d: Double): Pair<String, String> {
    if (sign(d) == -1.0) {
        return Pair(isProperLength(abs(d)), EASTING.EAST.name)
    }
    return Pair(isProperLength(abs(d)), EASTING.WEST.name)
}

fun angleType(angle: Double): Long {
    return round(angle).toLong()
}

fun isProperLength(distance: Double): String {
    val decimalFormat = DecimalFormat("#.###")
    //Float.valueOf(decimalFormat.format(f))
    /*      if (distance.toInt() <= 0) {
              return decimalFormat.format((distance * 100)) + " cm"
          }
          if (distance.toInt() <= 1000) {
              return decimalFormat.format((distance / 1000)) + " Km"
          }*/

    return decimalFormat.format(distance) + " m"

}