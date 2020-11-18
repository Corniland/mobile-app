package com.corniland.mobile.data.repository

import com.corniland.mobile.data.SessionManager
import com.corniland.mobile.data.model.LoginRequest
import com.corniland.mobile.data.model.RegisterRequest
import com.corniland.mobile.data.model.UpdateSettingsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor() {

    suspend fun login(email: String, password: String) = flow {
        val response = Repositories.api.login(LoginRequest(email, password))

        response.body()?.let {
            SessionManager.saveJwt(jwt = it.jwt)
            emit(true)
        } ?: run {
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun register(email: String, username: String, password: String) = flow {
        val response = Repositories.api.register(RegisterRequest(email, username, password))
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)

    suspend fun whoami() =
        flow { emit(Repositories.api.whoami().body()) }.flowOn(Dispatchers.IO)

    suspend fun getUser(id: String) =
        flow { emit(Repositories.api.getUser(id).body()) }.flowOn(Dispatchers.IO)

    fun updateSettings(username: String, password: String, privateProfile: Boolean) = flow {
        val response = Repositories.api.updateSettings(
            UpdateSettingsRequest(
                username = username,
                password = password,
                private_profile = privateProfile
            )
        )
        emit(response.code() == 200)
    }.flowOn(Dispatchers.IO)
}