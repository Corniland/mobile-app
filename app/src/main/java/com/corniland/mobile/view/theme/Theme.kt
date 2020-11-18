package com.corniland.mobile.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.graphics.Color

@Composable
fun CornilandTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val elevation = if (darkTheme) DarkElevation else LightElevation
    Providers(AmbientElevations provides elevation) {
        MaterialTheme(
            colors = if (darkTheme) darkColor else lightColor,
            shapes = shapes,
            content = content
        )
    }
}

// Colors
// ------

private val lightColor = lightColors(
    primary = gray900,
    secondary = gray700,
    background = gray100,
    onSecondary = gray200,
)

private val darkColor = darkColors(
    primary = gray100,
    secondary = gray300,
    background = gray900,
    onSecondary = gray800,
)

object CornilandTheme {

    @Composable
    val colors: Colors
        get() = MaterialTheme.colors

    @Composable
    val typography: Typography
        get() = MaterialTheme.typography

    @Composable
    val shapes: Shapes
        get() = MaterialTheme.shapes

    @Composable
    val elevations: Elevations
        get() = AmbientElevations.current

}