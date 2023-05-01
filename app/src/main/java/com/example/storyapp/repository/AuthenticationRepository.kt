package com.example.storyapp.repository

import com.example.storyapp.UserPreferences
import com.example.storyapp.apiNetwork.AuthenticationApi

class AuthenticationRepository(
    private val api: AuthenticationApi,
    private val preferences: UserPreferences
): BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.postLogin(email, password)
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) = safeApiCall {
        api.postRegister(name, email, password)
    }

    suspend fun saveTokenRepository(token: String){
        preferences.saveLoginToken(token)
    }

}