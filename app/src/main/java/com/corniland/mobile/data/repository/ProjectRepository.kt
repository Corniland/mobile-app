package com.corniland.mobile.data.repository

import android.util.Log
import androidx.compose.runtime.emit
import com.corniland.mobile.data.model.CreateProjectRequest
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.model.UpdateProjectRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Error

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
        response.body()?.let { emit(it) } ?: run { emit(null) }
    }.flowOn(Dispatchers.IO)

    fun likeProject(id: String) = flow {
        val response = Repositories.api.likeProject(id)
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)

    fun unlikeProject(id: String) = flow {
        val response = Repositories.api.unlikeProject(id)
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)

    fun createProject(title: String) = flow {
        val response = Repositories.api.createProject(CreateProjectRequest(title = title))
        response.body()?.let { emit(it) } ?: run { emit(null) }
    }.flowOn(Dispatchers.IO)

    fun updateProject(projectId: String, projectRequest: UpdateProjectRequest) = flow {
        val response = Repositories.api.updateProject(id = projectId, body = projectRequest)
        response.body()?.let { emit(it) } ?: run { emit(null) }
    }.flowOn(Dispatchers.IO)

    fun deleteProject(id: String) = flow {
        val response = Repositories.api.deleteProject(id = id)
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)

    fun addUserToProject(userId: String, projectId: String) = flow {
        val response = Repositories.api.addUserToProject(userId = userId, projectId = projectId)
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)

    fun removeUserToProject(userId: String, projectId: String) = flow {
        val response = Repositories.api.removeUserToProject(userId = userId, projectId = projectId)
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)
}
