package com.corniland.mobile.view.project

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.model.Project
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.corniland.mobile.data.repository.ProjectRepository
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.FullScreenFailedToLoad
import com.corniland.mobile.view.utils.FullScreenLoading
import com.corniland.mobile.view.utils.SwipeRefreshIndicator
import com.corniland.mobile.view.utils.SwipeToRefreshLayout

@ExperimentalMaterialApi
@Composable
fun ProjectBrowser() {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { ProjectBrowserViewModel(repository = repository) }
    val isLoading by viewModel.isLoading.observeAsState(false)
    val state: ProjectBrowserViewState by viewModel.projectRequest.observeAsState(
        ProjectBrowserViewState.Loading
    )

    SwipeToRefreshLayout(
        refreshingState = state !is ProjectBrowserViewState.Loading && isLoading, // not loading, but reloading
        onRefresh = { viewModel.refresh() },
        refreshIndicator = { SwipeRefreshIndicator() },
        content = {
            when (state) {
                is ProjectBrowserViewState.Loading -> FullScreenLoading()
                is ProjectBrowserViewState.Error -> FullScreenFailedToLoad()
                is ProjectBrowserViewState.Success -> ProjectList(projects = (state as ProjectBrowserViewState.Success).projects)
            }
        }
    )
}

@Composable
private fun ProjectList(projects: List<Project>) {
    ScrollableColumn {
        BigTitle()
        projects.forEach { project ->
            ProjectItem(
                project = project,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun BigTitle() {
    Text(
        "Project\nBrowser",
        style = CornilandTheme.typography.h2,
        color = CornilandTheme.colors.primary,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(16.dp)
    )
}
