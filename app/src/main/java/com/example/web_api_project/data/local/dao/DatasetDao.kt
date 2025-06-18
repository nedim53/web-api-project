package com.example.web_api_project.data.local.dao

import androidx.room.*
import com.example.web_api_project.data.local.entity.DatasetEntity

@Dao
interface DatasetDao {
    @Query("SELECT * FROM datasets")
    suspend fun getAll(): List<DatasetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(datasets: List<DatasetEntity>)

    @Query("DELETE FROM datasets")
    suspend fun clearAll()
} 