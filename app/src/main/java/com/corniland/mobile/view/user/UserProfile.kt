package com.corniland.mobile.view.user

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.FullScreenFailedToLoad
import com.corniland.mobile.view.utils.FullScreenLoading
import com.corniland.mobile.view.utils.NavigatorAmbient


@Composable
fun UserProfile(userId: String) {
    val repository = RepositoriesAmbient.current.user
    val viewModel = remember { UserProfileViewModel(repository, userId) }
    val state = viewModel.state.observeAsState(initial = UserProfileViewState.Loading)

    when (state.value) {
        is UserProfileViewState.Loading -> FullScreenLoading()
        is UserProfileViewState.Failed -> FullScreenFailedToLoad()
        is UserProfileViewState.Success -> ProfilePage(user = (state.value as UserProfileViewState.Success).user)
    }
}

@Composable
fun CurrentUserProfile() {
    val session = SessionManagerAmbient.current
    val user = session.state.observeAsState().value

    user?.let {
        ProfilePage(user = user, isCurrentUser = true)
    } ?: run {
        Text("No user is currently logged in.")
    }
}

@Composable
private fun ProfilePage(user: User, isCurrentUser: Boolean = false) {
    val navigator = NavigatorAmbient.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 48.dp)
    ) {
        Text("You are", style = CornilandTheme.typography.caption)
        Text(
            text = user.username,
            style = CornilandTheme.typography.h2,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 36.dp)
        )

        ListGroup {
            ListItem { Text(text = "Projects") }
            ListItem { Text(text = "Liked project (${user.liked_projects.count()})") }
        }

        if (isCurrentUser) {
            Spacer(Modifier.padding(top = 24.dp))
            ListGroup {
                ListItem(onClick = { navigator.navigate(Destination.UserSettings) }) {
                    Text("Edit profile")
                }
                ListItem { Text("New project") }
            }
        }
    }
}

@Composable
private fun ListItem(
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
private fun ListGroup(content: @Composable () -> Unit) {
    Surface(
        color = CornilandTheme.colors.onSecondary,
        shape = CornilandTheme.shapes.medium,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Column {
            content()
        }
    }
}