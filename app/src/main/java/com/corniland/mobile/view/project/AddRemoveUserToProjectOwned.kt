package com.corniland.mobile.view.project

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.*

@ExperimentalMaterialApi
@Composable
fun AddRemoveUserToProjectOwned(otherUserId: String) {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { AddRemoveUserToProjectOwnedViewModel(repository = repository) }
    val currentUser = SessionManagerAmbient.current.state.observeAsState().value
    val projectListState =
        viewModel.projectRequest.observeAsState(ViewStateResource.Loading()).value

    currentUser?.let { user ->
        when (projectListState) {
            is ViewStateResource.Loading -> FullScreenLoading()
            is ViewStateResource.Error -> FullScreenFailedToLoad()
            is ViewStateResource.Success -> ProjectList(
                projects = projectListState.item,
                currentUser = user,
                targetUser = otherUserId
            )
        }
    } ?: run {
        FullScreen {
            Text("You are no logged in")
        }
    }
}


@Composable
private fun ProjectList(
    projects: List<Project>,
    currentUser: User,
    targetUser: String
) {
    ScrollableColumn(Modifier.padding(16.dp)) {
        Text(
            text = "Select a project to add or remove this user from",
            style = CornilandTheme.typography.h3,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp, top = 48.dp, bottom = 32.dp)
        )

        if (projects.count() > 0) {
            ListGroup {
                projects
                    .filter { project -> project.owner == currentUser.id }
                    .forEach { project ->
                        ProjectLineWithAddRemoveMember(
                            projectId = project.id,
                            targetUser = targetUser
                        )
                    }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 36.dp)
            ) {
                Text(text = "Nothing to show there", style = CornilandTheme.typography.subtitle1)
                Icon(Icons.Default.Face)
            }
        }

    }
}

@Composable
private fun ProjectLineWithAddRemoveMember(projectId: String, targetUser: String) {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember {
        AddRemoveUserToProjectOwnedItemViewModel(
            repository = repository,
            projectId = projectId
        )
    }

    val projectState = viewModel.projectState.observeAsState(ViewStateResource.Loading()).value
    val addState = viewModel.addMemberState.observeAsState(ViewStateAction.Idle).value
    val removeState = viewModel.removeMemberState.observeAsState(ViewStateAction.Idle).value

    when (projectState) {
        is ViewStateResource.Loading -> LinearProgressIndicator()
        is ViewStateResource.Error -> Text("Failed to load", color = Color.Red)
        is ViewStateResource.Success -> {
            val project = projectState.item
            val isMemberInProject = project.members.contains(targetUser)

            if (addState is ViewStateAction.Success || removeState is ViewStateAction.Success) {
                viewModel.refresh()
            }

            ListItem {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = project.title,
                        style = CornilandTheme.typography.body1,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    if (isMemberInProject) {
                        when (removeState) {
                            ViewStateAction.Idle -> Button(onClick = {
                                viewModel.removeUserToProject(
                                    userId = targetUser,
                                    projectId = project.id
                                )
                            }, shape = CircleShape) {
                                Icon(Icons.Default.Clear)
                            }
                            ViewStateAction.Loading -> CircularProgressIndicator()
                            ViewStateAction.Failed -> Icon(Icons.Default.Warning)
                            ViewStateAction.Success -> Icon(Icons.Default.Check)
                        }
                    } else {
                        when (addState) {
                            ViewStateAction.Idle -> Button(onClick = {
                                viewModel.addUserToProject(
                                    userId = targetUser,
                                    projectId = project.id
                                )
                            }, shape = CircleShape) {
                                Icon(Icons.Default.Add)
                            }
                            ViewStateAction.Loading -> CircularProgressIndicator()
                            ViewStateAction.Failed -> Icon(Icons.Default.Warning)
                            ViewStateAction.Success -> Icon(Icons.Default.Check)
                        }
                    }
                }
            }
        }
    }
}
