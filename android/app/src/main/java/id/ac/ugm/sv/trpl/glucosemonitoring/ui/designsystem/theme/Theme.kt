package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = Blue.copy(0.75f),
    onPrimaryContainer = White,
    inversePrimary = Blue,
    
    secondary = Blue,
    onSecondary = White,
    secondaryContainer = Blue.copy(0.75f),
    onSecondaryContainer = White,
    
    tertiary = Blue,
    onTertiary = White,
    tertiaryContainer = Blue.copy(0.75f),
    onTertiaryContainer = White,
    
    background = White,
    onBackground = Black,
    surface = Grey.copy(0.15f),
    onSurface = Black,
    
    surfaceVariant = Grey.copy(0.15f),
    onSurfaceVariant = Black,
    surfaceTint = Blue.copy(0.15f),
    inverseSurface = Grey.copy(0.15f),
    inverseOnSurface = Black,
    
    error = Red,
    onError = White,
    errorContainer = Red,
    onErrorContainer = White,
    
    outline = Blue.copy(0.4f),
    outlineVariant = Grey.copy(0.4f),
    scrim = Black.copy(0.5f),
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    onPrimary = White,
    primaryContainer = Blue.copy(0.75f),
    onPrimaryContainer = White,
    inversePrimary = Blue,
    
    secondary = Blue,
    onSecondary = White,
    secondaryContainer = Blue.copy(0.75f),
    onSecondaryContainer = White,
    
    tertiary = Blue,
    onTertiary = White,
    tertiaryContainer = Blue.copy(0.75f),
    onTertiaryContainer = White,
    
    background = White,
    onBackground = Black,
    surface = Grey.copy(0.15f),
    onSurface = Black,
    
    surfaceVariant = Grey.copy(0.15f),
    onSurfaceVariant = Black,
    surfaceTint = Blue.copy(0.15f),
    inverseSurface = Grey.copy(0.15f),
    inverseOnSurface = Black,
    
    error = Red,
    onError = White,
    errorContainer = Red,
    onErrorContainer = White,
    
    outline = Blue.copy(0.4f),
    outlineVariant = Grey.copy(0.4f),
    scrim = Black.copy(0.5f),
)

@Composable
fun GlucoverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    fixBackgroundColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    /**
     *  GlucoverTheme with fixBackgroundColor was used to encapsulate the composable coming from
     *  the Sheets-Compose-Dialogs library because the dialog background was not as expected and
     *  the library provided no way to change it.
     */
    val darkColorScheme = if (fixBackgroundColor) {
        DarkColorScheme.copy(
            surface = White,
            secondaryContainer = Grey.copy(0.15f),
        )
    } else {
        DarkColorScheme
    }
    val lightColorScheme = if (fixBackgroundColor) {
        LightColorScheme.copy(
            surface = White,
            secondaryContainer = Grey.copy(0.15f),
        )
    } else {
        LightColorScheme
    }
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}