package com.example.storyapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ui.dashboard.DashboardViewModel
import com.example.storyapp.repository.AuthenticationRepository
import com.example.storyapp.repository.BaseRepository
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.ui.authentication.AuthenticationViewModel

class BaseViewModel(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(repository as AuthenticationRepository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(repository as StoryRepository) as T
            else -> throw java.lang.IllegalArgumentException("View Model Class NotFound")
        }
    }
}