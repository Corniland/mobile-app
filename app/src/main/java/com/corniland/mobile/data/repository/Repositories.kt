package com.corniland.mobile.data.repository

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf

data class Repositories(
    val project: ProjectRepository = ProjectRepository()
)

val RepositoriesAmbient : ProvidableAmbient<Repositories> = ambientOf()
