package com.corniland.mobile.view.project

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.corniland.mobile.view.main.Destination
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.view.theme.CornilandTheme
import com.corniland.mobile.view.utils.HorizontalRuler
import com.corniland.mobile.view.utils.ImageLoadFailed
import com.corniland.mobile.view.utils.ImageLoading
import com.corniland.mobile.view.utils.NavigatorAmbient
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProjectItem(project: Project, modifier: Modifier = Modifier) {
    val navigator = NavigatorAmbient.current

    Surface(
        color = CornilandTheme.colors.background,
        modifier = modifier.clickable(
            onClick = { navigator.navigate(Destination.ProjectDetails(project.id)) },
            indication = RippleIndication()
        )
    ) {
        Column {
            Row(Modifier.padding(16.dp)) {
                Column(Modifier.weight(fill = true, weight = 1f)) {
                    Title(project)
                    Description(project)
                }

                ProjectImage(project)
            }

            HorizontalRuler()
        }
    }
}

@Composable
private fun Title(project: Project) {
    ProvideEmphasis(emphasis = AmbientEmphasisLevels.current.high) {
        Text(
            project.title,
            style = CornilandTheme.typography.h5,
            color = CornilandTheme.colors.primary
        )
    }
}

@Composable
private fun Description(project: Project) {
    Text(
        project.short_description,
        style = CornilandTheme.typography.body1,
        color = CornilandTheme.colors.primary
    )
}

@Composable
private fun ProjectImage(project: Project) {
    Surface(shape = RoundedCornerShape(8.dp)) {
        GlideImage(
            imageModel = project.cover_picture_url,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(width = 112.dp, height = 64.dp),
            loading = { ImageLoading() },
            failure = { ImageLoadFailed() }
        )
    }
}

@Composable
private fun Details(project: Project) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Icon(
            Icons.Default.FavoriteBorder,
            tint = CornilandTheme.colors.secondary,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 4.dp),
        )

        Text(
            project.likes.toString(),
            style = CornilandTheme.typography.body2,
            color = CornilandTheme.colors.secondary
        )

        Spacer(Modifier.weight(1f))

        Text(
            project.status,
            style = CornilandTheme.typography.body2,
            color = CornilandTheme.colors.secondary
        )
    }
}

@Composable
fun Image(url: String, modifier: Modifier = Modifier) {
    GlideImage(
        imageModel = url,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .heightIn(max = 192.dp)
            .fillMaxWidth(),
        loading = { ImageLoading() },
        failure = { ImageLoadFailed() }
    )
}
