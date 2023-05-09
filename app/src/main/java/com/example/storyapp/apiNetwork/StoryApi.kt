package com.example.storyapp.apiNetwork

import com.example.storyapp.response.DetailStoryResponse
import com.example.storyapp.response.Story
import com.example.storyapp.response.StoryResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApi {
    @GET("stories")
    suspend fun getUserStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoryResponse

    @GET("stories/{userid}")
    fun getDetailStory(
        @Path("userId") userId: String
    ): DetailStoryResponse


    @POST("logout")
    suspend fun logout(): ResponseBody
}