package com.corniland.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.corniland.mobile.data.repository.Repositories
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.project.ProjectBrowser
import com.corniland.mobile.view.project.ProjectDetails
import com.corniland.mobile.view.utils.Navigator
import com.corniland.mobile.view.utils.NavigatorAmbient
import kotlinx.android.parcel.Parcelize

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CornilandApp(backDispatcher = onBackPressedDispatcher)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CornilandApp(backDispatcher: OnBackPressedDispatcher) {
    CornilandTheme {
        Background()
        AppBody(backDispatcher)
    }
}

@Composable
fun Background() {
    Surface(Modifier.fillMaxSize(), color = CornilandTheme.colors.background) {}
}

@ExperimentalMaterialApi
@Composable
fun AppBody(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> =
        rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
            Navigator(Destination.ProjectBrowser, backDispatcher)
        }

    Providers(NavigatorAmbient provides navigator, RepositoriesAmbient provides Repositories()) {
        Crossfade(current = navigator.current) { destination ->
            when (destination) {
                is Destination.ProjectBrowser -> ProjectBrowser()
                is Destination.ProjectDetails -> ProjectDetails(id = destination.projectId)
            }
        }
    }
}

sealed class Destination : Parcelable {

    @Parcelize
    object ProjectBrowser : Destination()

    @Parcelize
    data class ProjectDetails(val projectId: String) : Destination()
}
