package com.example.storyapp.repository

import com.example.storyapp.apiNetwork.StoryApi

class StoryRepository(
    private val api: StoryApi
): BaseRepository(){

    suspend fun getStories() = safeApiCall {
        api.getUserStories()
    }

    suspend fun getDetailStories(
        userId: String
    ) = safeApiCall {
        api.getDetailStory(userId)
    }

}