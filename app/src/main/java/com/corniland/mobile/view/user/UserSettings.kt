package com.corniland.mobile.view.user

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme

@Composable
fun UserSettings() {
    val session = SessionManagerAmbient.current
    val user = session.state.observeAsState().value

    user?.let {
        UserSettingsPage(user = user)
    } ?: run {
        Text("No user is currently logged in.")
    }
}

@Composable
private fun UserSettingsPage(user: User) {
    val repository = RepositoriesAmbient.current.user
    val viewModel = remember { UserSettingsViewModel(repository = repository) }
    val updateState = viewModel.state.observeAsState(UserSettingsViewState.Idle).value

    val username = remember { mutableStateOf(TextFieldValue(user.username)) }
    val newPassword = remember { mutableStateOf(TextFieldValue()) }
    val newPasswordConfirm = remember { mutableStateOf(TextFieldValue()) }
    val privateProfile = remember { mutableStateOf(user.private_profile) }

    val passwordMatch = { newPassword.value.text == newPasswordConfirm.value.text }
    val updateButtonEnabled = { passwordMatch() && updateState !is UserSettingsViewState.Loading }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "Edit profile",
            style = CornilandTheme.typography.h2,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        )

        if (updateState is UserSettingsViewState.Failed) {
            Text(
                "Failed to update settings",
                color = CornilandTheme.colors.error,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (updateState is UserSettingsViewState.Success) {
            Text(
                "Successfully updated the settings",
                color = CornilandTheme.colors.error,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") }
        )

        OutlinedTextField(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = { Text("New password") },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = newPasswordConfirm.value,
            onValueChange = { newPasswordConfirm.value = it },
            label = { Text("Confirm password") },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            isErrorValue = !passwordMatch() && newPasswordConfirm.value.text != "",
        )

        when (updateState) {
            is UserSettingsViewState.Failed,
            is UserSettingsViewState.Idle -> {
                Button(
                    onClick = {
                        viewModel.updateSettings(
                            username = username.value.text,
                            password = newPassword.value.text,
                            privateProfile = privateProfile.value
                        )
                    },
                    enabled = updateButtonEnabled(),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Update")
                }
            }

            is UserSettingsViewState.Loading -> CircularProgressIndicator(Modifier.padding(24.dp))
            is UserSettingsViewState.Success -> Icon(
                modifier = Modifier.padding(24.dp),
                asset = Icons.Default.Check
            )
        }

    }
}