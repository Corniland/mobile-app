package com.corniland.mobile.view

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerState
import androidx.compose.material.Surface
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.corniland.mobile.Destination
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.HorizontalRuler
import com.corniland.mobile.view.utils.NavigatorAmbient

@Composable
fun Drawer(drawerState: DrawerState) {
    Providers(DrawerStateAmbient provides drawerState) {
        Column(Modifier.padding(top = 32.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Corniland",
                    style = CornilandTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }

            Column {
                MenuItem("Browse", destination = Destination.ProjectBrowser)
                /*HorizontalRuler()
                MenuItem("My project")
                HorizontalRuler()
                MenuItem("My profile")*/
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight().padding(bottom = 64.dp)
            ) {
                MenuItem("Login", destination = Destination.Login)
            }
        }
    }
}

@Composable
fun MenuItem(name: String, destination: Destination) {
    val navigator = NavigatorAmbient.current
    val drawerState = DrawerStateAmbient.current

    Surface(
        color = if (navigator.current == destination) CornilandTheme.colors.background else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = RippleIndication(),
                onClick = {
                    navigator.navigate(destination)
                    drawerState.close()
                }
            )
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = name,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 16.dp)
            )
        }
    }
}

private val DrawerStateAmbient: ProvidableAmbient<DrawerState> = ambientOf()