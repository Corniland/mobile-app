package com.corniland.mobile.view.utils

import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.corniland.mobile.view.theme.CornilandTheme


@Composable
fun ImageLoading() {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val indicator = createRef()
        CircularProgressIndicator(
            modifier = Modifier.constrainAs(indicator) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}

@Composable
fun ImageLoadFailed() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CornilandTheme.colors.onSecondary
    ) {
        Icon(Icons.Default.Warning)
    }
}