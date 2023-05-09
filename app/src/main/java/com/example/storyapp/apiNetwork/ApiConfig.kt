package com.example.storyapp.apiNetwork

import com.example.storyapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
        private const val AUTH_HEADER = "Authorization"

        private var uploadToken = ""

        fun setuploadToken(value: String) {
            uploadToken = value
        }
        private fun getUploadToken(): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()

                if(request.header("No-Authentication") == null) {
                    if(uploadToken.isNotEmpty()) {
                        val uploadToken = "Bearer $uploadToken"
                        request = request.newBuilder()
                            .addHeader(AUTH_HEADER, uploadToken)
                            .build()
                    }
                }
                chain.proceed(request)
            }
        }

        fun getUploadStoryApi(): UploadStoryApi {
            val loggingInterceptor = if (androidx.viewbinding.BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(getUploadToken())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(UploadStoryApi::class.java)
        }
    }

    fun<Api> apiClient(
        api: Class<Api>,
        authenticationToken: String? = null
    ): Api {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor{ chain ->  
                    chain.proceed(chain
                        .request()
                        .newBuilder()
                        .also {
                            it.addHeader("Authorization", "Bearer $authenticationToken")
                        }.build())
                }
                .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                    client.addInterceptor(logging)
                }
            }.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}