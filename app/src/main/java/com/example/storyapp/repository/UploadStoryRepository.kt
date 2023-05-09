package com.example.storyapp.repository

import com.example.storyapp.apiNetwork.UploadStoryApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryRepository(
    private val api: UploadStoryApi
): BaseRepository() {

    suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody
    ) = safeApiCall {
        api.postStory(file,description)
    }
}