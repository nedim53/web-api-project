package com.example.web_api_project.data.remote.deaths

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

interface DeathsApiService {
    @Headers("Content-Type: application/json")
    @POST("api/DiedByRequestDate/list")
    suspend fun getDeathsData(
        @Body request: DeathsRequest
    ): Response<DeathsResponse>
} 