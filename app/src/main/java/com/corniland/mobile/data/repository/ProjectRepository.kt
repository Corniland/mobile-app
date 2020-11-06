package com.corniland.mobile.data.repository

import com.corniland.mobile.data.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProjectRepository {

    suspend fun getProjects() = flow {
        val response = Repositories.api.getAllProjects()

        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
            emit(response.body() ?: emptyList<Project>())
        } else {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getProject(id: String) = flow {
        val response = Repositories.api.getProject(id)

        response.body()?.let {
            emit(it)
        } ?: run {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)
}
