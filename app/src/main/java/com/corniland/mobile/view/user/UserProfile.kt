package com.corniland.mobile.view.user

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.*

@Composable
fun UserProfile(userId: String) {
    val repository = RepositoriesAmbient.current.user
    val viewModel = remember { UserProfileViewModel(repository, userId) }
    val state = viewModel.state.observeAsState(initial = ViewStateResource.Loading())

    when (state.value) {
        is ViewStateResource.Loading -> FullScreenLoading()
        is ViewStateResource.Error -> FullScreenFailedToLoad()
        is ViewStateResource.Success -> ProfilePage(user = (state.value as ViewStateResource.Success).item)
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

        ListGroup(modifier = Modifier.padding(16.dp)) {
            ListItem(onClick = {
                navigator.navigate(
                    Destination.ProjectBrowser(
                        title = "Project owned by ${user.username}",
                        byOwner = user.id
                    )
                )
            }) {
                Text(text = if (isCurrentUser) "My projects" else "Project of ${user.username}")
            }

            ListItem(onClick = {
                navigator.navigate(
                    Destination.ProjectBrowser(
                        title = "Favourite project of ${user.username}",
                        byIds = user.liked_projects
                    )
                )
            }) {
                Text(text = "Liked project (${user.liked_projects.count()})")
            }
        }

        if (isCurrentUser) {
            Spacer(Modifier.padding(top = 24.dp))
            ListGroup(Modifier.padding(16.dp)) {
                ListItem(onClick = { navigator.navigate(Destination.UserSettings) }) {
                    Text("Edit profile")
                }

                ListItem(onClick = { navigator.navigate(Destination.CreateNewProject) }) {
                    Text("New project")
                }
            }
        }
    }
}