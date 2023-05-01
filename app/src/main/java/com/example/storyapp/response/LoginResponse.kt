package com.example.storyapp.response

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)