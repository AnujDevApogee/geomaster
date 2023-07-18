package com.apogee.geomaster.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.text.bold
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.gson.Gson


fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

object ApiUtils {
    val POST_GET_TABLE_RECORDS =
        Pair("http://120.138.10.146:8080/BLE_ProjectV6_2/resources/getAllTableRecords/", 102)
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

fun setHtmlBoldTxt(txt: String): SpannableStringBuilder {
    val span = SpannableStringBuilder(txt)
    val bss = StyleSpan(Typeface.BOLD); // Span to make text bold
    span.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return span
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