package com.corniland.mobile.view.login

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.NavigatorAmbient

@Composable
fun Login() {
    val repository = RepositoriesAmbient.current.user
    val sessionManager = SessionManagerAmbient.current
    val viewModel = remember { LoginViewModel(repository = repository) }
    val navigator = NavigatorAmbient.current

    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val loginState = viewModel.state.observeAsState(LoginViewState.Idle).value

    if (loginState is LoginViewState.SuccessLogin) {
        sessionManager.updateCurrentUser()
        Handler(Looper.getMainLooper()).postDelayed(
            { navigator.navigate(Destination.ProjectBrowser) },
            1000
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            "Login",
            style = CornilandTheme.typography.h2,
            color = CornilandTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 48.dp, bottom = 8.dp)
        )

        if (loginState is LoginViewState.FailedLogin) {
            Text(
                "Failed to login",
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
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Crossfade(current = loginState) {
            when (loginState) {
                LoginViewState.Idle,
                LoginViewState.FailedLogin ->
                    Button(
                        onClick = {
                            viewModel.performLogin(
                                email = email.value.text,
                                password = password.value.text
                            )
                        },
                        enabled = loginState is LoginViewState.Idle || loginState is LoginViewState.FailedLogin,
                        modifier = Modifier.padding(top = 16.dp),
                    ) {
                        Text("Login")
                    }

                LoginViewState.Loading -> CircularProgressIndicator(Modifier.padding(24.dp))
                LoginViewState.SuccessLogin -> Icon(
                    modifier = Modifier.padding(24.dp),
                    asset = Icons.Default.Check
                )
            }
        }

        TextButton(
            onClick = { navigator.navigate(Destination.Register) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Don't have an account yet ?")
        }

    }

}

@Preview
@Composable
private fun PreviewLoginLight() {
    CornilandTheme(darkTheme = false) {
        Scaffold(
            bodyContent = { Login() }
        )
    }
}

@Preview
@Composable
private fun PreviewLoginDark() {
    CornilandTheme(darkTheme = true) {
        Scaffold(
            bodyContent = { Login() }
        )
    }
}


