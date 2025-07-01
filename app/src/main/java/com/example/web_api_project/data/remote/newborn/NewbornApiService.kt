package com.example.web_api_project.data.remote.newborn

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

interface NewbornApiService {
    @Headers("Content-Type: application/json")
    @POST("api/NewbornByRequestDate/List")
    suspend fun getNewbornData(
        @Body request: NewbornRequest,
        @retrofit2.http.Query("q") query: String? = null
    ): Response<NewbornResponse>
} 