package com.example.storyapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.response.DetailStoryResponse
import com.example.storyapp.response.Story
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DashboardViewModel (
    private val repository: StoryRepository
    ): BaseViewModel(repository) {


        private val _userDetailStory: MutableLiveData<Resource<DetailStoryResponse>> =  MutableLiveData()
        val userDetailStory: LiveData<Resource<DetailStoryResponse>>
            get() = _userDetailStory

        fun getDetailStory(
            userId: String
        ) = viewModelScope.launch {
            _userDetailStory.value = Resource.Loading
            _userDetailStory.value = repository.getDetailStories(userId)
        }

//        val quote: LiveData<PagingData<Story>> =
//            repository.getStories().cachedIn(viewModelScope)

        private val _userStory: MutableLiveData<Resource<StoryResponse>> = MutableLiveData()
        val userStory: LiveData<Resource<StoryResponse>>
            get() = _userStory

        fun getUserStories(
        ) = viewModelScope.launch {
            _userStory.value = Resource.Loading
            _userStory.value = repository.getStories()
        }

    }