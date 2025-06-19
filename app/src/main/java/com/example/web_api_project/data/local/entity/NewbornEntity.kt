package com.example.web_api_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newborn")
data class NewbornEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entity: String,
    val canton: String?,
    val municipality: String?,
    val institution: String?,
    val year: Int,
    val month: Int,
    val dateUpdate: String,
    val maleTotal: Int,
    val femaleTotal: Int,
    val total: Int,
    val isFavorite: Boolean = false
) 