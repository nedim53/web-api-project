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
    fun getNewborns(token: String, year: Int, entity: String, municipality: String? = null, searchQuery: String? = null): Flow<NewbornResource> = flow {
        emit(NewbornResource.Loading)
        try {
            val local = dao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) emit(NewbornResource.Success(local))
            val response = api.getNewbornData(
                NewbornRequest(year = year.toString(), month = null, entity = entity, municipality = municipality.takeIf { !it.isNullOrBlank() }),
                query = searchQuery
            )
            if (response.isSuccessful) {
                val data = response.body()?.result ?: emptyList()
                data.forEach { entry ->
                    dao.upsert(entry.toEntity(isFavorite = false))
                }
                emit(NewbornResource.Success(data.map { entry ->
                    entry.copy(isFavorite = false)
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