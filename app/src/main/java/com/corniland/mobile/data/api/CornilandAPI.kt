package com.corniland.mobile.data.api

import com.corniland.mobile.data.model.Project
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CornilandAPI {

    @GET("projects")
    suspend fun getAllProjects(): Response<List<Project>>

    @GET("projects/{id}")
    suspend fun getProject(@Path("id") id: String): Response<Project>

}
