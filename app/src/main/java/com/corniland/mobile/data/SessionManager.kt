package com.corniland.mobile.data

import android.content.Context
import android.content.SharedPreferences
import com.corniland.mobile.MainActivity
import com.corniland.mobile.R

class SessionManager {
    private var prefs: SharedPreferences = MainActivity.instance.getSharedPreferences(
        MainActivity.instance.applicationContext.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    fun saveJwt(jwt: String) {
        val editor = prefs.edit()
        editor.putString(JWT_TOKEN, jwt)
        editor.apply()
    }

    fun getJwt(): String? {
        return prefs.getString(JWT_TOKEN, null)
    }

    companion object {
        const val JWT_TOKEN = "user_token"
    }

}