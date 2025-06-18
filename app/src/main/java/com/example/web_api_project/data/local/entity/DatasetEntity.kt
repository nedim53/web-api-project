package com.example.web_api_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "datasets")
data class DatasetEntity(
    @PrimaryKey val id: String,
    val naziv: String?,
    val vrijednost: String?,
    val kategorija: String?
) 