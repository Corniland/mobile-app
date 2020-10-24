package com.corniland.mobile.view.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp

private val RefreshDistance = 100.dp

@ExperimentalMaterialApi
@Composable
fun SwipeToRefreshLayout(
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    refreshIndicator: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val refreshDistance = with(DensityAmbient.current) { RefreshDistance.toPx() }

    val state = rememberSwipeableState(refreshingState) { newValue ->
        if (newValue && !refreshingState)
            onRefresh()
        true
    }

    Box(
        modifier = Modifier.swipeable(
            state = state,
            anchors = mapOf(
                -refreshDistance to false,
                refreshDistance to true
            ),
            thresholds = { _, _ -> FractionalThreshold(0.5f) },
            orientation = Orientation.Vertical
        )
    ) {
        content()
        Box(Modifier.align(Alignment.TopCenter).offsetPx(y = state.offset)) {
            if (state.offset.value != -refreshDistance) {
                refreshIndicator()
            }
        }

        onCommit(refreshingState) {
            state.animateTo(refreshingState)
        }
    }
}

@Composable
fun SwipeRefreshIndicator() {
    Surface(elevation = 10.dp, shape = CircleShape) {
        CircularProgressIndicator(
            modifier = Modifier
                .preferredSize(36.dp)
                .padding(4.dp)
        )
    }
}
