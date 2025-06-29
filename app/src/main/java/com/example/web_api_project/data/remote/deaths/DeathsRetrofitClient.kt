package com.example.web_api_project.data.remote.deaths

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DeathsRetrofitClient {
    private const val BASE_URL = "https://odp.iddeea.gov.ba:8096/"
    private const val TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUxMTk4OTYwLCJleHAiOjE3NTEyODUzNjAsImlhdCI6MTc1MTE5ODk2MH0.sbXr05EksmvyRgW1p-rGDgmHrURJMz8CDBKwQb8bvd5RvY96XSnyY2QRJa68N87vUQGWn_fJzzjUH7NtcQRKxA"

    fun getClient(token: String = TOKEN): DeathsApiService {
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
            .create(DeathsApiService::class.java)
    }
} 