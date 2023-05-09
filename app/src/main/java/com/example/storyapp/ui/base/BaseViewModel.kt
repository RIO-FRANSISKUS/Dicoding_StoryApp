package com.example.storyapp.ui.base

import androidx.lifecycle.ViewModel
import com.example.storyapp.apiNetwork.StoryApi
import com.example.storyapp.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val repository: BaseRepository
): ViewModel() {

    suspend fun logout(
        api: StoryApi
    ) = withContext(Dispatchers.IO){repository.logout(api)}

}