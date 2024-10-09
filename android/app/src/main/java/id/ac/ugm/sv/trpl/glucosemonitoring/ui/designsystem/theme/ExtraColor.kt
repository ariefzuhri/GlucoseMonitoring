@file:Suppress("UnusedReceiverParameter")

package id.ac.ugm.sv.trpl.glucosemonitoring.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.green: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Green else Green

val ColorScheme.orange: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Orange else Orange

val ColorScheme.red: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Red else Red

val ColorScheme.yellow: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Yellow else Yellow

val ColorScheme.violet: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Violet else Violet

val ColorScheme.teal: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Teal else Teal