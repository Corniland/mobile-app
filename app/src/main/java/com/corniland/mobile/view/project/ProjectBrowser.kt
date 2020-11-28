package com.corniland.mobile.view.project

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
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
import com.corniland.mobile.view.utils.*

@ExperimentalMaterialApi
@Composable
fun ProjectBrowser(
    title: String? = null,
    byIds: List<String>? = null,
    byOwner: String? = null
) {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { ProjectBrowserViewModel(repository = repository) }
    val isLoading by viewModel.isLoading.observeAsState(false)
    val state: ViewStateResource<List<Project>> by viewModel.projectRequest.observeAsState(
        ViewStateResource.Loading()
    )

    SwipeToRefreshLayout(
        refreshingState = state !is ViewStateResource.Loading && isLoading, // not loading, but reloading
        onRefresh = { viewModel.refresh() },
        refreshIndicator = { SwipeRefreshIndicator() },
        content = {
            when (state) {
                is ViewStateResource.Loading -> FullScreenLoading()
                is ViewStateResource.Error -> FullScreenFailedToLoad()
                is ViewStateResource.Success -> {
                    val projects = (state as ViewStateResource.Success).item.filter { project ->
                        byIds?.contains(project.id)
                            ?: byOwner?.let { owner -> project.owner.contains(owner) }
                            ?: true
                    }

                    ProjectList(title = title, projects = projects)
                }
            }
        }
    )
}

@Composable
private fun ProjectList(title: String?, projects: List<Project>) {
    ScrollableColumn {
        BigTitle(title)

        if (projects.count() > 0) {
            projects.forEach { project ->
                ProjectItem(
                    project = project,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
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
private fun BigTitle(title: String?) {
    Text(
        text = title ?: "Project\nBrowser",
        style = CornilandTheme.typography.h2,
        color = CornilandTheme.colors.primary,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(16.dp, top = 48.dp)
    )
}
