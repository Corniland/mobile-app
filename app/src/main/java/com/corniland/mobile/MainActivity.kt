package com.corniland.mobile

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.repository.Repositories
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.Drawer
import com.corniland.mobile.view.login.Login
import com.corniland.mobile.view.login.Register
import com.corniland.mobile.view.project.ProjectBrowser
import com.corniland.mobile.view.project.ProjectDetails
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.Navigator
import com.corniland.mobile.view.utils.NavigatorAmbient
import kotlinx.android.parcel.Parcelize

class MainActivity : AppCompatActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this

        setContent {
            CornilandApp(backDispatcher = onBackPressedDispatcher)
        }
    }

    companion object {
        lateinit var instance: MainActivity private set
    }

}

@ExperimentalMaterialApi
@Composable
fun CornilandApp(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> =
        rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
            Navigator(Destination.ProjectBrowser, backDispatcher)
        }

    val scaffoldState = rememberScaffoldState()

    Providers(NavigatorAmbient provides navigator, RepositoriesAmbient provides Repositories()) {
        CornilandTheme {
            Scaffold(
                scaffoldState = scaffoldState,
                drawerShape = RoundedCornerShape(size = 0.dp),
                drawerContent = { Drawer(scaffoldState.drawerState) },
                bodyContent = {
                    AppBody()
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AppBody() {
    val navigator = NavigatorAmbient.current
    Crossfade(current = navigator.current) { destination ->
        when (destination) {
            is Destination.ProjectBrowser -> ProjectBrowser()
            is Destination.ProjectDetails -> ProjectDetails(id = destination.projectId)
            is Destination.Login -> Login()
            is Destination.Register -> Register()
        }
    }
}

sealed class Destination : Parcelable {

    @Parcelize
    object ProjectBrowser : Destination()

    @Parcelize
    data class ProjectDetails(val projectId: String) : Destination()

    @Parcelize
    object Login : Destination()

    @Parcelize
    object Register: Destination()
}
