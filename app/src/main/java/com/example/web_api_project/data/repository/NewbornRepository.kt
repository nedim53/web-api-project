package com.example.web_api_project.data.repository

import com.example.web_api_project.data.local.dao.NewbornDao
import com.example.web_api_project.data.local.entity.NewbornEntity
import com.example.web_api_project.data.remote.newborn.NewbornApiService
import com.example.web_api_project.data.remote.newborn.NewbornRequest
import com.example.web_api_project.data.remote.newborn.NewbornEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class NewbornResource {
    object Loading : NewbornResource()
    data class Success(val data: List<NewbornEntry>) : NewbornResource()
    data class Error(val message: String) : NewbornResource()
}

class NewbornRepository(
    private val api: NewbornApiService,
    private val dao: NewbornDao
) {
    fun getNewborns(token: String, year: Int, entity: String): Flow<NewbornResource> = flow {
        emit(NewbornResource.Loading)
        try {
            val local = dao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) emit(NewbornResource.Success(local))
            val response = api.getNewbornData(NewbornRequest(year = year.toString(), month = null, entity = entity))
            if (response.isSuccessful) {
                val data = response.body()?.result ?: emptyList()
                val oldFavorites = dao.getFavorites()
                val favoriteKeys = oldFavorites.map { it.uniqueKey() }.toSet()
                dao.clearAll()
                dao.insertAll(data.map { entry ->
                    val isFav = entry.uniqueKey() in favoriteKeys
                    entry.toEntity(isFavorite = isFav)
                })
                emit(NewbornResource.Success(data.map { entry ->
                    val isFav = entry.uniqueKey() in favoriteKeys
                    entry.copy(isFavorite = isFav)
                }))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(NewbornResource.Error("Greška: code=${response.code()}, message=${response.message()}, errorBody=$errorBody"))
            }
        } catch (e: Exception) {
            val local = dao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) {
                emit(NewbornResource.Success(local))
                emit(NewbornResource.Error("Nema interneta. Prikazani su poslednji sačuvani podaci."))
            } else {
                emit(NewbornResource.Error("Greška: ${e.localizedMessage}"))
            }
        }
    }

    suspend fun getFavorites(): List<NewbornEntry> = dao.getFavorites().map { it.toDomain() }
    suspend fun setFavorite(id: Int) = dao.setFavorite(id)
    suspend fun unsetFavorite(id: Int) = dao.unsetFavorite(id)
    suspend fun toggleFavorite(id: Int, isFav: Boolean) {
        if (isFav) dao.unsetFavorite(id) else dao.setFavorite(id)
    }

    fun observeFavorites(): Flow<List<NewbornEntry>> = dao.observeFavorites().map { list -> list.map { it.toDomain() } }

    suspend fun toggleFavoriteByFields(entity: String, municipality: String?, year: Int, month: Int, isFav: Boolean) {
        dao.setFavoriteByFields(entity, municipality, year, month, !isFav)
    }
}

private fun NewbornEntry.uniqueKey() = "${entity}_${municipality}_${year}_${month}"
private fun NewbornEntity.uniqueKey() = "${entity}_${municipality}_${year}_${month}"

private fun NewbornEntity.toDomain() = NewbornEntry(
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
    isFavorite = isFavorite
)

private fun NewbornEntry.toEntity(isFavorite: Boolean = false) = NewbornEntity(
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
    isFavorite = isFavorite || isFavorite
) 