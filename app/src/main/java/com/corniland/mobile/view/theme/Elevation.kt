package com.corniland.mobile.view.theme

import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Elevations(val card: Dp = 0.dp)

val AmbientElevations = staticAmbientOf { Elevations() }

val LightElevation = Elevations(card = 1.dp)

val DarkElevation = Elevations(card = 1.dp)
