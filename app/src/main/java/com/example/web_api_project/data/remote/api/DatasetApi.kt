package com.example.web_api_project.data.remote.api

import com.example.web_api_project.data.remote.dto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.room.*

interface DatasetApi {
    @GET("api/3/action/datastore_search")
    suspend fun getDatasets(
        @Query("resource_id") resourceId: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("q") query: String? = null
    ): ApiResponse
} 