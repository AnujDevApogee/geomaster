package com.apogee.geomaster.utils

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

object ApiUtils {
    val POST_GET_TABLE_RECORDS =
        Pair("http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getAllTableRecords/", 102)
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

fun isInvalidString(txt: String?) = txt.isNullOrEmpty() || txt.isBlank() || txt == "null"


fun Activity.changeStatusBarColor(color: Int) {
    this.window?.statusBarColor = getColorInt(color)
}

fun Activity.getColorInt(color: Int): Int {
    return resources.getColor(color, null)
}

fun <T> toJson(t: T): String {
    return Gson().toJson(t)
}


inline fun <reified T> fromJson(str: String): T {
    return Gson().fromJson(str, T::class.java)
}

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}


fun View.invisible() {
    this.visibility = View.INVISIBLE
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


fun getEmojiByUnicode(unicode: Int) = String(Character.toChars(unicode))

fun Activity.closeKeyboard(view: View) {
    val imm = (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}


fun Activity.openKeyBoard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    view.requestFocus()
}


fun Fragment.showMessage(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction)
    }
}


fun NavController.safeNavigate(direction: Int) {
    currentDestination?.getAction(direction)?.run {
        navigate(direction)
    }
}

fun createLog(tag: String, msg: String) {
    Log.i(tag, "createLog: $msg")
}


fun TextView.changeIconDrawable(id:Int,color: Int=R.color.holo_green_dark,position:Int=1){
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


    var isAlertFirst = true


}

