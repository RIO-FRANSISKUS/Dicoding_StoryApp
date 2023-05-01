package com.example.storyapp.apiNetwork

import com.example.storyapp.response.StoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StoryApi {

    @JvmSuppressWildcards
    @GET("stories")
    suspend fun getUserStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoryResponse
}