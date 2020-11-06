package com.corniland.mobile.view.project

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.*
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalMaterialApi
@Composable
fun ProjectDetails(id: String) {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { ProjectDetailsViewModel(projectId = id, repository = repository) }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val state by viewModel.projectRequest.observeAsState(ProjectDetailsViewState.Loading)

    SwipeToRefreshLayout(
        refreshingState = state !is ProjectDetailsViewState.Loading && isLoading, // not loading, but reloading
        onRefresh = { viewModel.refresh() },
        refreshIndicator = { SwipeRefreshIndicator() },
        content = {
            when (state) {
                is ProjectDetailsViewState.Loading -> FullScreenLoading()
                is ProjectDetailsViewState.Error -> FullScreenFailedToLoad()
                is ProjectDetailsViewState.Success -> ProjectPage(project = (state as ProjectDetailsViewState.Success).project)
            }
        }
    )
}

@Composable
private fun ProjectPage(project: Project) {
    ScrollableColumn {
        HeaderImage(url = project.cover_picture_url)
        CardProjectDetails(project)
    }
}

@Composable
private fun HeaderImage(url: String) {
    GlideImage(
        imageModel = url,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .heightIn(max = 192.dp)
            .fillMaxWidth(),
        loading = { ImageLoading() },
        failure = {
            Text("Failed to load the image")
        }
    )
}

@Composable
fun CardProjectDetails(project: Project) {
    Column(Modifier.padding(8.dp)) {
        Text(
            project.title,
            style = CornilandTheme.typography.h4,
            color = CornilandTheme.colors.primary
        )

        Text(
            project.description,
            style = CornilandTheme.typography.body1,
            color = CornilandTheme.colors.primary
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
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
    }
}
