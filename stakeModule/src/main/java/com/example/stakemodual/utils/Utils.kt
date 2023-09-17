package com.example.stakemodual.utils

import android.R
import android.graphics.Typeface
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

fun createLog(tag: String, msg: String) {
    Log.i(tag, "createLog: $msg")
}

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}



fun setHtmlTxt(txt: String, color: String): Spanned {
    return Html.fromHtml(
        "<font color=$color>$txt</font>", HtmlCompat.FROM_HTML_MODE_COMPACT
    )
}

fun setHtmlBoldTxt(txt: String): SpannableString {
    val ss = SpannableString(txt)
    val boldSpan = StyleSpan(Typeface.BOLD)
    ss.setSpan(boldSpan, 0, txt.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ss
}


fun Fragment.showMessage(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}


fun TextView.changeIconDrawable(id:Int, color: Int= R.color.holo_green_dark, position:Int=1){
    val tintColor = ContextCompat.getColor(context, color)
    var drawable = ContextCompat.getDrawable(context, id)
    drawable = DrawableCompat.wrap(drawable!!)
    DrawableCompat.setTint(drawable.mutate(), tintColor)
    drawable.setBounds( 0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    when(position){
        1->setCompoundDrawables(drawable, null, null, null)
        2->setCompoundDrawables(null, drawable, null, null)
        3->setCompoundDrawables(null, null, drawable, null)
        4->setCompoundDrawables(null, null, null, drawable)
    }
}

object StakeHelper {
    const val LONGITUDE = "LONGITUDE"
    const val LATITUDE = "LATITUDE"
    const val ELEVATION = "ELEVATION"
    const val XAXIS = "XAXIS"
    const val YAXIS = "YAXIS"
    const val ZAXIS = "ZAXIS"
    const val ANGLE = "ANGLE"
    const val DISTANCE = "DISTANCE"
    const val NORTH_SOUTH = "NORTH_SOUTH"
    const val EAST_WEST = "EAST_WEST"
}