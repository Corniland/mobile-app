package com.corniland.mobile.data.repository

import com.corniland.mobile.data.api.CornilandAPI
import com.corniland.mobile.data.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectRepository {

    private val api: CornilandAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.corniland.ovh/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.api = retrofit.create(CornilandAPI::class.java)
    }

    suspend fun getProjects() = flow {
        val response = api.getAllProjects()

        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
            emit(response.body() ?: emptyList<Project>())
        } else {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getProject(id: String) = flow {
        val response = api.getProject(id)

        response.body()?.let {
            emit(it)
        } ?: run {
            emit(null)
        }
    }.flowOn(Dispatchers.IO)
}
