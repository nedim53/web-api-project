package com.example.web_api_project.data.remote.newborn

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewbornRetrofitClient {
    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUwMjY5OTE4LCJleHAiOjE3NTAzNTYzMTgsImlhdCI6MTc1MDI2OTkxOH0.P5Q6T786VSUZmanQDMKBN60wtjB6QT8dRyLGc-e_XROEaaMVrIdp8VweqIIZWIWlUkrB8chKxztxD7BIbi6A6w"

    fun getClient(token: String = TOKEN): NewbornApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewbornApiService::class.java)
    }
} 