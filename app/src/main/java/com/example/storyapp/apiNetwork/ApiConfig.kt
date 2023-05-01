package com.example.storyapp.apiNetwork

import com.example.storyapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
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
                        .request())
                        .newBuilder()
                        .also {
                            it.addHeader("Authorization", "Bearer $authenticationToken")
                        }.build()
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