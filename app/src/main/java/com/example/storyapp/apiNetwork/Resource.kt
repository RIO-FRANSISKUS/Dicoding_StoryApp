package com.example.storyapp.apiNetwork

sealed class Resource<out T> {
    data class SucsessResponse<out T>(val value: T) : Resource<T>()

    data class FailResponse(
        val errorBody: String?
    ): Resource<Nothing>()

    object Loading : Resource<Nothing>()
}