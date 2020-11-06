package com.corniland.mobile.view.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.corniland.mobile.view.theme.CornilandTheme

@Composable
fun HorizontalRuler(color: Color = CornilandTheme.colors.secondary, width: Dp = 1.dp) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        color = color
    ) {
        Spacer(modifier = Modifier.height(width))
    }
}