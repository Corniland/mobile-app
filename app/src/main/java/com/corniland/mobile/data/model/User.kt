package com.corniland.mobile.data.model

import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

data class User(
    val id: String,
    val email: String,
    val username: String,
    val liked_projects: List<String>,
    val private_profile: Boolean = false,
    val banned: Boolean = false
) {
    companion object {
        fun getMock() = User(
            id = "507f191e810c19729de860ea",
            email = "foo@example.com",
            username = "FooBar",
            liked_projects = listOf("5f6e96852f1bc609ad3c55de", "5f6e98d4c4bb195ebf77a6d2"),
            private_profile = false,
            banned = false,
        )

        private val possiblesColors = listOf(
            Color.Red, Color.Blue, Color.Yellow,
            Color.LightGray, Color.Green, Color.Cyan,
            Color.Magenta, Color.Gray
        )

        fun getColorFor(userId: String): Color {
            return possiblesColors[abs(userId.hashCode()) % possiblesColors.count()]
        }
    }
}

data class LoginRequest(
    var email: String,
    var password: String
)

data class LoginResponse(
    var jwt: String
)

data class RegisterRequest(
    var email: String,
    var username: String,
    var password: String,
)

data class UpdateSettingsRequest(
    var username: String,
    var password: String,
    var private_profile: Boolean
)