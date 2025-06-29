package com.example.web_api_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // id iz UserEntity
    val newbornId: Int? = null, // id iz NewbornEntity
    val deathsId: Int? = null, // id iz DeathsEntity
    val dataType: String // "newborn" ili "deaths"
) 