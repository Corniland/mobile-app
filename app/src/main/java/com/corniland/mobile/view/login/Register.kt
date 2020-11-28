package com.corniland.mobile.view.login

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
import androidx.compose.material.Scaffold
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
import androidx.ui.tooling.preview.Preview
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.NavigatorAmbient
import com.corniland.mobile.view.utils.ViewStateAction

@Composable
fun Register() {
    val repository = RepositoriesAmbient.current.user
    val viewModel = remember { RegisterViewModel(repository = repository) }
    val registerState = viewModel.state.observeAsState(ViewStateAction.Idle).value
    val navigator = NavigatorAmbient.current

    val email = remember { mutableStateOf(TextFieldValue()) }
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val passwordRepeat = remember { mutableStateOf(TextFieldValue()) }

    val passwordMatch = { password.value.text == passwordRepeat.value.text }
    val registerButtonEnabled = {
        email.value.text.isNotBlank() &&
        username.value.text.isNotBlank() &&
        password.value.text.isNotBlank() &&
        passwordRepeat.value.text.isNotBlank() &&
        passwordMatch() &&
        (registerState is ViewStateAction.Idle || registerState is ViewStateAction.Failed)
    }

    if (registerState is ViewStateAction.Success) {
        // Send the user to home once he's registered, 1sec delay to show the checkmark for the success
        Handler(Looper.getMainLooper()).postDelayed({ navigator.navigate(Destination.Login) }, 1000)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            "Register",
            style = CornilandTheme.typography.h2,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        )

        if (registerState is ViewStateAction.Failed) {
            Text(
                "Failed to register",
                color = CornilandTheme.colors.error,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            keyboardType = KeyboardType.Email,
        )

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            keyboardType = KeyboardType.Text,
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = passwordRepeat.value,
            onValueChange = { passwordRepeat.value = it },
            label = { Text("Repeat password") },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            isErrorValue = !passwordMatch() && passwordRepeat.value.text != "",
        )

        when (registerState) {
            is ViewStateAction.Failed,
            is ViewStateAction.Idle -> {
                Button(
                    onClick = {
                        viewModel.performRegister(
                            username = username.value.text,
                            email = email.value.text,
                            password = password.value.text
                        )
                    },
                    enabled = registerButtonEnabled(),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Register")
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

@Preview
@Composable
private fun PreviewRegisterLight() {
    CornilandTheme(darkTheme = false) {
        Scaffold(
            bodyContent = { Register() }
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterDark() {
    CornilandTheme(darkTheme = true) {
        Scaffold(
            bodyContent = { Register() }
        )
    }
}
