package com.corniland.mobile.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.lifecycle.MutableLiveData
import com.corniland.mobile.view.main.MainActivity
import com.corniland.mobile.R
import com.corniland.mobile.data.model.User
import com.corniland.mobile.data.repository.Repositories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SessionManager @Inject constructor(val repositories: Repositories) {

    val state: MutableLiveData<User?> = MutableLiveData(null)

    init {
        updateCurrentUser()
    }

    fun updateCurrentUser() {
        CoroutineScope(Dispatchers.IO).launch {
            repositories.user.whoami()
                .collect {
                    state.postValue(it)
                }
        }
    }

    fun logout() {
        saveJwt(null)
        state.postValue(null)
    }

    companion object {
        private val prefs: SharedPreferences by lazy {
            MainActivity.instance.getSharedPreferences(
                MainActivity.instance.applicationContext.getString(R.string.app_name),
                Context.MODE_PRIVATE
            )
        }

        fun saveJwt(jwt: String?) {
            val editor = prefs.edit()
            editor.putString(JWT_TOKEN, jwt)
            editor.apply()
        }

        fun getJwt(): String? {
            return prefs.getString(JWT_TOKEN, null)
        }

        const val JWT_TOKEN = "jwt_token"
    }
}

val SessionManagerAmbient: ProvidableAmbient<SessionManager> = ambientOf()
