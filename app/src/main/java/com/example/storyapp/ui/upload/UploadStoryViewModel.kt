package com.example.storyapp.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.apiNetwork.ApiConfig
import com.example.storyapp.response.UploadStoryResponse
import com.example.storyapp.util.reduceImageSize
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadStoryViewModel: ViewModel() {

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _hasUploaded = MutableLiveData<File>()
    val hasUploaded: LiveData<File> = _hasUploaded

    fun setFile(value: File) {
        _hasUploaded.value = value
    }


    fun postStory(
        file: File,
        description: String
    ) {
        val compressedFile = file.reduceImageSize()
        val desc = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            compressedFile.name,
            requestImageFile
        )

        val api = ApiConfig.getUploadStoryApi().postStory(imageMultipart, desc)
        _isLoading.value = true
        api.enqueue(object : Callback<UploadStoryResponse> {
            override fun onResponse(
                call: Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>
            ){
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isPosted.value = true
                        Log.d(TAG, responseBody.message)
                    }
                } else {
                    _isPosted.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isPosted.value = false
                Log.e(TAG, "onFailure: Gagal")
            }
        })


    }

    companion object {
        private const val TAG = "UploadStoryViewModel"
    }

}