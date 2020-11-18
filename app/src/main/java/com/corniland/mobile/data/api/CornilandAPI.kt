package com.corniland.mobile.data.api

import com.corniland.mobile.data.model.*
import retrofit2.Response
import retrofit2.http.*


interface CornilandAPI {

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<User>

    @PUT("api/users/me")
    suspend fun updateSettings(@Body body: UpdateSettingsRequest): Response<User>

    @GET("api/projects")
    suspend fun getAllProjects(): Response<List<Project>>

    @GET("api/projects/{id}")
    suspend fun getProject(@Path("id") id: String): Response<Project>

    @POST("auth/user/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("auth/user/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>

    @GET("auth/user/me")
    suspend fun whoami(): Response<User>
}
