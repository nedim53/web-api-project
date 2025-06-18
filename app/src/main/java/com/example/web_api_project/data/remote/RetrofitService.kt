package com.example.web_api_project.data.remote

import com.example.web_api_project.BuildConfig
import com.example.web_api_project.data.remote.api.DatasetApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitService {
    
    private const val BASE_URL = "https://odp.iddeea.gov.ba/"
    
    private val authInterceptor = Interceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.ODP_API_TOKEN}")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()
    
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
    
    val datasetApi: DatasetApi = retrofit.create(DatasetApi::class.java)
} 