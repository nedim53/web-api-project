package com.example.web_api_project.data.remote.newborn

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewbornRetrofitClient {
    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUwMzY0MjI4LCJleHAiOjE3NTA0NTA2MjgsImlhdCI6MTc1MDM2NDIyOH0.HjrfZQ2QERU8fe7nGszcC-B8vBJxjG4R7bQV5UvTnfAmNm9vjePXzOGX2A5bZZTGRWwbUrd9SWTZiE7bzTG20Q"

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