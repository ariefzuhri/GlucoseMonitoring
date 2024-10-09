package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context?.showToast(@StringRes message: Int) {
    Toast.makeText(this?.applicationContext, message, Toast.LENGTH_LONG).show()
}