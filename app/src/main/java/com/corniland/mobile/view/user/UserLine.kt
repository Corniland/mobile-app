package com.corniland.mobile.view.user

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.ViewStateResource

@Composable
fun UserLine(userId: String, onClick: () -> Unit = {}) {
    val repository = RepositoriesAmbient.current.user
    val viewModel = remember { UserLineViewModel(repository, userId) }
    val state = viewModel.state.observeAsState(initial = ViewStateResource.Loading()).value

    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
            .clickable(
                indication = RippleIndication(),
                onClick = onClick,
                enabled = state is ViewStateResource.Success
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 24.dp, end = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserAvatar(userId = userId)
                Spacer(Modifier.weight(1f))

                when (state) {
                    is ViewStateResource.Loading -> LinearProgressIndicator()
                    is ViewStateResource.Error -> Text("Failed to load")
                    is ViewStateResource.Success -> UserName(state.item)
                }
            }
        }
    }
}

@Composable
private fun UserName(user: User) {
    Text(
        text = user.username,
        style = CornilandTheme.typography.body1,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(end = 8.dp)
    )
    Icon(Icons.Default.KeyboardArrowRight)
}

@Composable
private fun UserAvatar(userId: String) {
    Surface(
        shape = CircleShape,
        color = User.getColorFor(userId = userId)
    ) {
        Icon(Icons.Default.Person, Modifier.padding(8.dp))
    }
}