package com.corniland.mobile.view.main

import android.os.Parcelable
import androidx.compose.animation.Crossfade
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.corniland.mobile.view.login.Login
import com.corniland.mobile.view.login.Register
import com.corniland.mobile.view.project.ProjectBrowser
import com.corniland.mobile.view.project.ProjectDetails
import com.corniland.mobile.view.user.CurrentUserProfile
import com.corniland.mobile.view.user.UserProfile
import com.corniland.mobile.view.user.UserSettings
import com.corniland.mobile.view.utils.NavigatorAmbient
import kotlinx.android.parcel.Parcelize


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
            is Destination.UserProfile -> UserProfile(userId = destination.userId)
            is Destination.CurrentUserProfile -> CurrentUserProfile()
            is Destination.UserSettings -> UserSettings()
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
    object Register : Destination()

    @Parcelize
    data class UserProfile(val userId: String) : Destination()

    @Parcelize
    object CurrentUserProfile : Destination()

    @Parcelize
    object UserSettings: Destination()

}
