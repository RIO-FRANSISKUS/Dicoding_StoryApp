package com.example.storyapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.apiNetwork.ApiConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.prefDataStore by preferencesDataStore("token_data_store")

class UserPreferences constructor(context: Context) {

    private val userDataStore = context.prefDataStore
    private val LOGIN_TOKEN = stringPreferencesKey("login_token")

    val loginToken: Flow<String?>
        get() = userDataStore.data.map { preferences ->
            preferences[LOGIN_TOKEN]
        }


    suspend fun saveLoginToken(loginToken : String){
        userDataStore.edit { preferences ->
            preferences[LOGIN_TOKEN] = loginToken
        }
    }

    suspend fun clearUserLogin(){
        userDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun setToken(token: String) {
        ApiConfig.setuploadToken(token)
    }

}