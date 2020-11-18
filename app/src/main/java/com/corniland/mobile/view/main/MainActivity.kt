package com.corniland.mobile.view.main

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.corniland.mobile.data.SessionManager
import com.corniland.mobile.data.SessionManagerAmbient
import com.corniland.mobile.data.repository.Repositories
import com.corniland.mobile.data.repository.RepositoriesAmbient
import com.corniland.mobile.view.Drawer
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.Navigator
import com.corniland.mobile.view.utils.NavigatorAmbient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repositories: Repositories

    @Inject
    lateinit var sessionManager: SessionManager

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this

        setContent {
            Providers(
                RepositoriesAmbient provides repositories,
                SessionManagerAmbient provides sessionManager
            ) {
                CornilandApp(backDispatcher = onBackPressedDispatcher)
            }
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

    Providers(NavigatorAmbient provides navigator) {
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