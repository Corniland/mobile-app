package com.corniland.mobile.view.project

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.user.UserLine
import com.corniland.mobile.view.utils.*
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalMaterialApi
@Composable
fun ProjectDetails(id: String) {
    val repository = RepositoriesAmbient.current.project
    val session = SessionManagerAmbient.current
    val viewModel = remember {
        ProjectDetailsViewModel(
            projectId = id,
            repository = repository,
            session = session
        )
    }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val state by viewModel.projectRequest.observeAsState()

    SwipeToRefreshLayout(
        refreshingState = state !is ViewStateResource.Loading && isLoading, // not loading, but reloading
        onRefresh = { viewModel.refresh() },
        refreshIndicator = { SwipeRefreshIndicator() },
        content = {
            when (state) {
                is ViewStateResource.Loading -> FullScreenLoading()
                is ViewStateResource.Error -> FullScreenFailedToLoad()
                is ViewStateResource.Success -> ProjectPage(
                    project = (state as ViewStateResource.Success<Project>).item,
                    viewModel = viewModel
                )
            }
        }
    )
}

private val headerSize = 192.dp

@Composable
private fun ProjectPage(project: Project, viewModel: ProjectDetailsViewModel) {
    val currentUser = SessionManagerAmbient.current.state.observeAsState().value
    val isOwner = project.owner == currentUser?.id

    ScrollableColumn {
        Box(modifier = Modifier.fillMaxWidth()) {
            HeaderImage(url = project.cover_picture_url)
            ButtonLike(project, viewModel)
        }

        CardProjectDetails(project)

        if (isOwner) {
            OwnerActionsButton(project = project, viewModel = viewModel)
        }

        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}

@Composable
fun OwnerActionsButton(project: Project, viewModel: ProjectDetailsViewModel) {
    val navigator = NavigatorAmbient.current
    val openConfirmDialog = remember { mutableStateOf(false) }
    val deleteState = viewModel.deleteRequest.observeAsState().value

    if (deleteState is ViewStateAction.Success) {
        navigator.navigate(Destination.ProjectBrowser())
    }

    if (deleteState is ViewStateAction.Failed) {
        Text(
            "Failed to delete the project",
            color = Color.Red,
            modifier = Modifier.padding(start = 32.dp)
        )
    }

    Text(
        text = "Owner options",
        style = CornilandTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 32.dp, top = 24.dp, bottom = 8.dp)
    )

    ListGroup(modifier = Modifier.padding(start = 32.dp, end = 32.dp)) {
        ListItem(onClick = { navigator.navigate(Destination.EditProject(projectId = project.id)) }) {
            Text("Edit project")
        }
        ListItem(onClick = { openConfirmDialog.value = true }) {
            if (deleteState is ViewStateAction.Loading) {
                CircularProgressIndicator()
            } else {
                Text("Delete project", color = Color.Red)
            }
        }
    }

    if (openConfirmDialog.value) {
        AlertDialog(
            title = { Text("Are you sure to delete this project ?") },
            onDismissRequest = { openConfirmDialog.value = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.delete()
                    openConfirmDialog.value = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { openConfirmDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}

@Composable
private fun HeaderImage(url: String) {
    GlideImage(
        imageModel = url,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .heightIn(max = headerSize)
            .fillMaxWidth(),
        loading = { ImageLoading() },
        failure = { ImageLoadFailed() }
    )
}

@Composable
fun CardProjectDetails(project: Project) {
    val navigator = NavigatorAmbient.current

    Column(Modifier.padding(32.dp)) {
        Text(
            project.title,
            style = CornilandTheme.typography.h3,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            project.description,
            style = CornilandTheme.typography.body1,
            color = CornilandTheme.colors.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Icon(
                Icons.Default.FavoriteBorder,
                tint = CornilandTheme.colors.secondary,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 4.dp),
            )

            Text(
                project.likes.toString(),
                style = CornilandTheme.typography.body2,
                color = CornilandTheme.colors.secondary
            )

            Spacer(Modifier.weight(1f))

            Text(
                project.status,
                style = CornilandTheme.typography.body2,
                color = CornilandTheme.colors.secondary
            )
        }


        Text(
            text = "Members",
            style = CornilandTheme.typography.subtitle1,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        ListGroup {
            Spacer(modifier = Modifier.padding(top = 16.dp))
            project.members.forEach { userId ->
                UserLine(userId = userId, onClick = {
                    navigator.navigate(Destination.UserProfile(userId = userId))
                })
            }
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Composable
fun ButtonLike(project: Project, viewModel: ProjectDetailsViewModel) {
    val currentUser = SessionManagerAmbient.current.state.observeAsState().value
    val isLikingIt = currentUser?.liked_projects?.contains(project.id) ?: false
    val likeRequest = viewModel.likeRequest.observeAsState()

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth().padding(top = headerSize - 24.dp, end = 32.dp)
    ) {
        FloatingActionButton(
            onClick = {
                if (isLikingIt) {
                    viewModel.unlikeProject(onCompleted = { viewModel.refresh() })
                } else {
                    viewModel.likeProject(onCompleted = { viewModel.refresh() })
                }
            },
            icon = {
                when (likeRequest.value) {
                    is ViewStateAction.Loading -> CircularProgressIndicator(color = CornilandTheme.colors.background)
                    is ViewStateAction.Failed -> Icon(Icons.Default.Warning)
                    is ViewStateAction.Idle, is ViewStateAction.Success ->
                        Icon(if (isLikingIt) Icons.Default.Favorite else Icons.Default.FavoriteBorder)
                }
            }
        )
    }
}