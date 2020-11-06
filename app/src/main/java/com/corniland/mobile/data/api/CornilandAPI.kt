package com.corniland.mobile.data.api

import com.corniland.mobile.data.model.LoginRequest
import com.corniland.mobile.data.model.LoginResponse
import com.corniland.mobile.data.model.Project
import com.corniland.mobile.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.*


interface CornilandAPI {

    @GET("api/projects")
    suspend fun getAllProjects(): Response<List<Project>>

    @GET("api/projects/{id}")
    suspend fun getProject(@Path("id") id: String): Response<Project>

    @POST("auth/user/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("auth/user/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>

}
