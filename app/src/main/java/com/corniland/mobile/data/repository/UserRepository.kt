package com.corniland.mobile.data.repository

import com.corniland.mobile.data.SessionManager
import com.corniland.mobile.data.api.CornilandAPI
import com.corniland.mobile.data.model.LoginRequest
import com.corniland.mobile.data.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    suspend fun login(email: String, password: String) = flow {
        val response = Repositories.api.login(LoginRequest(email, password))

        response.body()?.let {
            SessionManager().saveJwt(jwt = it.jwt)
            emit(true)
        } ?: run {
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(email: String, username: String, password: String) = flow {
        val response = Repositories.api.register(RegisterRequest(email, username, password))
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)
}