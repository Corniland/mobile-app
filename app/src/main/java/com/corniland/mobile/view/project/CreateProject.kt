package com.corniland.mobile.view.project

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.NavigatorAmbient
import com.corniland.mobile.view.utils.ViewStateActionResponse

@Composable
fun CreateProject() {
    val repository = RepositoriesAmbient.current.project
    val viewModel = remember { CreateProjectViewModel(repository = repository) }
    val createState = viewModel.state.observeAsState(ViewStateActionResponse.Idle()).value
    val navigator = NavigatorAmbient.current

    val title = remember { mutableStateOf(TextFieldValue()) }

    val createButtonEnabled = { title.value.text.isNotBlank() }

    if (createState is ViewStateActionResponse.Success) {
        Handler(Looper.getMainLooper())
            .postDelayed({
                val projectId = createState.item.id
                navigator.navigate(Destination.ProjectDetails(projectId = projectId))
            }, 1000)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            "Create a new project",
            textAlign = TextAlign.Center,
            style = CornilandTheme.typography.h2,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        )

        if (createState is ViewStateActionResponse.Failed) {
            Text(
                "Failed to create project",
                color = CornilandTheme.colors.error,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (createState is ViewStateActionResponse.Success) {
            Text(
                "Successfully created the project",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            keyboardType = KeyboardType.Text
        )

        when (createState) {
            is ViewStateActionResponse.Failed,
            is ViewStateActionResponse.Idle -> {
                Button(
                    onClick = {
                        viewModel.performCreate(title = title.value.text)
                    },
                    enabled = createButtonEnabled(),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Create project")
                }
            }

            is ViewStateActionResponse.Loading -> CircularProgressIndicator(Modifier.padding(24.dp))
            is ViewStateActionResponse.Success -> Icon(
                modifier = Modifier.padding(24.dp),
                asset = Icons.Default.Check
            )
        }

    }

}