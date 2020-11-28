package com.corniland.mobile.data.api

import com.corniland.mobile.data.model.*
import retrofit2.Response
import retrofit2.http.*


interface CornilandAPI {

    // User

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<User>

    @PUT("api/users/me")
    suspend fun updateSettings(@Body body: UpdateSettingsRequest): Response<User>

    // Project

    @GET("api/projects")
    suspend fun getAllProjects(): Response<List<Project>>

    @GET("api/projects/{id}")
    suspend fun getProject(@Path("id") id: String): Response<Project>

    @POST("api/projects/{id}/like")
    suspend fun likeProject(@Path("id") id: String): Response<Unit>

    @DELETE("api/projects/{id}/like")
    suspend fun unlikeProject(@Path("id") id: String): Response<Unit>

    @POST("api/projects")
    suspend fun createProject(@Body body: CreateProjectRequest): Response<Project>

    @PUT("api/projects/{id}")
    suspend fun updateProject(@Path("id") id: String, @Body body: UpdateProjectRequest): Response<Project>

    @DELETE("api/projects/{id}")
    suspend fun deleteProject(@Path("id") id: String): Response<Unit>

    @POST("api/projects/{project_id}/member/{user_id}")
    suspend fun addUserToProject(@Path("project_id") projectId: String, @Path("user_id") userId: String): Response<Unit>

    @DELETE("api/projects/{project_id}/member/{user_id}")
    suspend fun removeUserToProject(@Path("project_id") projectId: String, @Path("user_id") userId: String): Response<Unit>

    // Auth

    @POST("auth/user/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("auth/user/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>

    @GET("auth/user/me")
    suspend fun whoami(): Response<User>
}
