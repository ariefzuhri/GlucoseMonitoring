package id.ac.ugm.sv.trpl.glucosemonitoring.ui.common.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Int.toPainter(): Painter {
    return painterResource(this)
}

@Composable
fun Int.toText(): String {
    return stringResource(this)
}

@Composable
fun TextStyle.toTypeface(): Typeface {
    val resolver: FontFamily.Resolver = LocalFontFamilyResolver.current
    return remember(resolver, this) {
        resolver.resolve(
            fontFamily = this.fontFamily,
            fontWeight = this.fontWeight ?: FontWeight.Normal,
            fontStyle = this.fontStyle ?: FontStyle.Normal,
            fontSynthesis = this.fontSynthesis ?: FontSynthesis.All,
        )
    }.value as Typeface
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    fun Context.findActivity(): Activity? {
        return when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
    }
    
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}