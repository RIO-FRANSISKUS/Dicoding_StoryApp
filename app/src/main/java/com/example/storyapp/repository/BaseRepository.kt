package com.example.storyapp.repository

import com.example.storyapp.apiNetwork.Resource
import com.example.storyapp.apiNetwork.StoryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T>{
        return withContext(Dispatchers.IO){
            try {
                Resource.SucsessResponse(apiCall.invoke())
            }catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.FailResponse("error")
                    }
                    else -> {
                        Resource.FailResponse(null)
                    }
                }
            }
        }
    }


    suspend fun logout(api: StoryApi) = safeApiCall {
        api.logout()
    }
}