package com.example.storyapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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

}