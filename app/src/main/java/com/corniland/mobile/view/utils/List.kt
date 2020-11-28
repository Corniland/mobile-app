package com.corniland.mobile.view.utils

import androidx.compose.foundation.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.corniland.mobile.view.theme.CornilandTheme

@Composable
fun ListItem(
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = RippleIndication(),
                onClick = onClick
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 16.dp)
        ) {
            Row {
                content()
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.KeyboardArrowRight)
            }
        }
    }
}

@Composable
fun ListGroup(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        color = CornilandTheme.colors.onSecondary,
        shape = CornilandTheme.shapes.medium,
        modifier = modifier
    ) {
        Column {
            content()
        }
    }
}