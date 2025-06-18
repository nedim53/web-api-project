package com.example.web_api_project.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.web_api_project.data.local.dao.DatasetDao
import com.example.web_api_project.data.local.entity.DatasetEntity

@Database(entities = [DatasetEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun datasetDao(): DatasetDao
} 