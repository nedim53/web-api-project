package com.example.web_api_project.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.web_api_project.data.remote.deaths.DeathsEntry

@Entity(tableName = "deaths")
data class DeathsEntity(
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

fun DeathsEntity.toDomain(): DeathsEntry = DeathsEntry(
    id = id,
    entity = entity,
    canton = canton,
    municipality = municipality,
    institution = institution,
    year = year,
    month = month,
    dateUpdate = dateUpdate,
    maleTotal = maleTotal,
    femaleTotal = femaleTotal,
    total = total,
    isFavorite = false
) 