package com.corniland.mobile.view.project

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.model.UpdateProjectRequest
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.*

@Composable
fun EditProject(id: String) {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { EditProjectViewModel(repository = repository, projectId = id) }
    val projectRequest = viewModel.projectRequest.observeAsState(ViewStateResource.Loading()).value

    when (projectRequest) {
        is ViewStateResource.Loading -> FullScreenLoading()
        is ViewStateResource.Error -> FullScreenFailedToLoad()
        is ViewStateResource.Success -> EditProjectPage(
            viewModel = viewModel,
            project = projectRequest.item
        )
    }
}

@Composable
fun EditProjectPage(viewModel: EditProjectViewModel, project: Project) {
    val title = remember { mutableStateOf(TextFieldValue(project.title)) }
    val shortDescription = remember { mutableStateOf(TextFieldValue(project.short_description)) }
    val description = remember { mutableStateOf(TextFieldValue(project.description)) }
    val status = remember { mutableStateOf(TextFieldValue(project.status)) }
    val coverPictureUrl = remember { mutableStateOf(TextFieldValue(project.cover_picture_url)) }
    val published = remember { mutableStateOf(project.published) }

    val updateRequest = viewModel.actionRequest.observeAsState(ViewStateAction.Idle).value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Edit project",
            style = CornilandTheme.typography.h2,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        )

        if (updateRequest is ViewStateAction.Failed) {
            Text(
                "Failed to update project",
                color = CornilandTheme.colors.error,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (updateRequest is ViewStateAction.Success) {
            Text(
                "Successfully updated the project",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") }
        )

        OutlinedTextField(
            value = shortDescription.value,
            onValueChange = { shortDescription.value = it },
            label = { Text("Short description") }
        )

        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Description") }
        )

        OutlinedTextField(
            value = status.value,
            onValueChange = { status.value = it },
            label = { Text("Status") }
        )

        OutlinedTextField(
            value = coverPictureUrl.value,
            onValueChange = { coverPictureUrl.value = it },
            label = { Text("Cover picture URL") }
        )

        SwitchWithLabel(
            label = "Published",
            checked = published.value,
            onCheckedChange = { published.value = it }
        )

        when (updateRequest) {
            is ViewStateAction.Failed,
            is ViewStateAction.Idle -> {
                Button(
                    onClick = {
                        viewModel.update(
                            projectId = project.id,
                            UpdateProjectRequest(
                                title = title.value.text,
                                short_description = shortDescription.value.text,
                                description = description.value.text,
                                status = status.value.text,
                                cover_picture_url = coverPictureUrl.value.text,
                                published = published.value
                            )
                        )
                    },
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Update")
                }
            }

            is ViewStateAction.Loading -> CircularProgressIndicator(Modifier.padding(24.dp))
            is ViewStateAction.Success -> Icon(
                modifier = Modifier.padding(24.dp),
                asset = Icons.Default.Check
            )
        }
    }

}