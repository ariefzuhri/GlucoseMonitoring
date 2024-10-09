package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.graphics.Color
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

fun Int.alpha(alpha: Float): Int {
    return Color.argb((alpha * 255).toInt(), red, green, blue)
}

fun View.shouldGone(shouldGone: Boolean) {
    visibility = if (shouldGone) View.GONE else View.VISIBLE
}