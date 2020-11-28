package com.corniland.mobile.view.main

import android.os.Parcelable
import androidx.compose.animation.Crossfade
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.corniland.mobile.view.login.Login
import com.corniland.mobile.view.login.Register
import com.corniland.mobile.view.project.CreateProject
import com.corniland.mobile.view.project.EditProject
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
            is Destination.ProjectBrowser -> ProjectBrowser(
                title = destination.title,
                byIds = destination.byIds,
                byOwner = destination.byOwner
            )
            is Destination.ProjectDetails -> ProjectDetails(id = destination.projectId)
            is Destination.Login -> Login()
            is Destination.Register -> Register()
            is Destination.UserProfile -> UserProfile(userId = destination.userId)
            is Destination.CurrentUserProfile -> CurrentUserProfile()
            is Destination.UserSettings -> UserSettings()
            is Destination.CreateNewProject -> CreateProject()
            is Destination.EditProject -> EditProject(id = destination.projectId)
        }
    }
}

sealed class Destination : Parcelable {

    @Parcelize
    data class ProjectBrowser(
        val title: String? = null,
        val byIds: List<String>? = null,
        val byOwner: String? = null
    ) : Destination()

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
    object UserSettings : Destination()

    @Parcelize
    object CreateNewProject : Destination()

    @Parcelize
    data class EditProject(val projectId: String) : Destination()

}
