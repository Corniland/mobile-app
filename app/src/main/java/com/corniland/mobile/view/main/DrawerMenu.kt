package com.corniland.mobile.view.main

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.HorizontalRuler
import com.corniland.mobile.view.utils.NavigatorAmbient

@Composable
fun Drawer(drawerState: DrawerState) {
    val session = SessionManagerAmbient.current
    val currentUser = session.state.observeAsState().value
    val navigator = NavigatorAmbient.current

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

                currentUser?.username?.let {
                    MenuItem(destination = Destination.CurrentUserProfile) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp)
                        ) {
                            Text(text = it, style = CornilandTheme.typography.h4)
                            Text(
                                "Currently connected as",
                                style = CornilandTheme.typography.caption
                            )
                        }
                    }
                    Spacer(Modifier.padding(bottom = 24.dp))
                }
            }

            Column {
                MenuItem(destination = Destination.ProjectBrowser()) { MenuTitle(name = "Browse") }

                currentUser?.let { user ->
                    HorizontalRuler(verticalPadding = 8.dp)

                    MenuItem(
                        destination = Destination.ProjectBrowser(
                            title = "Favourite project of ${user.username}",
                            byOwner = user.id
                        )
                    ) {
                        MenuTitle("My project")
                    }

                    HorizontalRuler(verticalPadding = 8.dp)

                    MenuItem(destination = Destination.CurrentUserProfile) {
                        MenuTitle("My profile")
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight().padding(bottom = 64.dp)
        ) {
            currentUser?.let {
                MenuItem(onClick = {
                    session.logout()
                    navigator.navigate(Destination.Login)
                }) {
                    MenuTitle(name = "Logout")
                }
            } ?: run {
                MenuItem(destination = Destination.Login) { MenuTitle(name = "Login") }
            }
        }
    }
}

@Composable
private fun MenuItem(destination: Destination, content: @Composable () -> Unit) {
    val navigator = NavigatorAmbient.current

    MenuItem(
        onClick = { navigator.navigate(destination) },
        color = if (navigator.current == destination) CornilandTheme.colors.background else Color.Transparent
    ) {
        content()
    }
}

@Composable
private fun MenuItem(
    onClick: () -> Unit,
    color: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val drawerState = DrawerStateAmbient.current

    Surface(
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = RippleIndication(),
                onClick = {
                    onClick()
                    drawerState.close()
                }
            )
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            content()
        }
    }
}

@Composable
private fun MenuTitle(name: String) {
    Text(
        text = name,
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 16.dp)
    )
}

private val DrawerStateAmbient: ProvidableAmbient<DrawerState> = ambientOf()