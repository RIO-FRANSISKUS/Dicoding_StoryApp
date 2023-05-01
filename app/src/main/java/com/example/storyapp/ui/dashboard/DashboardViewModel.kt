package com.example.storyapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.repository.AuthenticationRepository
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class DashboardViewModel (
    private val repository: StoryRepository
    ): ViewModel() {

        val storyPage = 1
        private val _userStory: MutableLiveData<Resource<StoryResponse>> = MutableLiveData()
        val userStory: LiveData<Resource<StoryResponse>>
            get() = _userStory

        fun getUserStories(
        ) = viewModelScope.launch {
            _userStory.value = Resource.Loading
            _userStory.value = repository.getStories()

        }

        private fun handleStoryResponse(response: Response<StoryResponse>): Resource<StoryResponse> {
            if (response.isSuccessful){
                response.body()?.let {
                    return Resource.SucsessResponse(it)
                }
            }

            return Resource.FailResponse(response.message())
        }

    }