package com.example.pickandroll.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val MAIN_ELEMENT_SIZE = 350.dp

private val DarkColorPalette = darkColors(
    primary = lightBlue,
    primaryVariant = Color.White,
    secondary = orange,
    background = slate,

    onPrimary = slate,
    onBackground = lightBlue,
    onSecondary = slate
)

@Composable
fun PickAndRollTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}