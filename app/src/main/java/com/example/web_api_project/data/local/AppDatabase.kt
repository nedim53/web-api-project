package com.example.web_api_project.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.web_api_project.data.local.dao.DatasetDao
import com.example.web_api_project.data.local.entity.DatasetEntity
import com.example.web_api_project.data.local.entity.UserEntity
import com.example.web_api_project.data.local.dao.UserDao
import com.example.web_api_project.data.local.entity.NewbornEntity
import com.example.web_api_project.data.local.dao.NewbornDao
import com.example.web_api_project.data.local.entity.FavoriteEntity
import com.example.web_api_project.data.local.dao.FavoriteDao
import com.example.web_api_project.data.local.entity.DeathsEntity
import com.example.web_api_project.data.local.dao.DeathsDao

@Database(entities = [DatasetEntity::class, UserEntity::class, NewbornEntity::class, FavoriteEntity::class, DeathsEntity::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun datasetDao(): DatasetDao
    abstract fun userDao(): UserDao
    abstract fun newbornDao(): NewbornDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun deathsDao(): DeathsDao
} 