package com.example.web_api_project.data.remote.newborn

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewbornRetrofitClient {
    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUxMjg4NTcxLCJleHAiOjE3NTEzNzQ5NzEsImlhdCI6MTc1MTI4ODU3MX0.UMP-vDUID1omaG0uEAioFB5TKEVilJlpEh5joF_yj4J3qPeWz-fCpfPg5MWyjEaYL662wkAsonxeRYH2DAkocg"

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