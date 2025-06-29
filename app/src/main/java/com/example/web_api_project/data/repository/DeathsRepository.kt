package com.example.web_api_project.data.repository

import com.example.web_api_project.data.local.dao.DeathsDao
import com.example.web_api_project.data.local.entity.DeathsEntity
import com.example.web_api_project.data.remote.deaths.DeathsApiService
import com.example.web_api_project.data.remote.deaths.DeathsRequest
import com.example.web_api_project.data.remote.deaths.DeathsEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class DeathsResource {
    object Loading : DeathsResource()
    data class Success(val data: List<DeathsEntry>) : DeathsResource()
    data class Error(val message: String) : DeathsResource()
}

class DeathsRepository(
    private val api: DeathsApiService,
    private val dao: DeathsDao
) {
    fun getDeaths(token: String, year: Int, entity: String, municipality: String? = null): Flow<DeathsResource> = flow {
        emit(DeathsResource.Loading)
        try {
            val local = dao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) emit(DeathsResource.Success(local))
            
            val response = api.getDeathsData(DeathsRequest(
                year = year.toString(), 
                month = null, 
                entity = entity, 
                municipality = municipality.takeIf { !it.isNullOrBlank() }
            ))
            
            if (response.isSuccessful) {
                val data = response.body()?.result ?: emptyList()
                data.forEach { entry ->
                    dao.upsert(entry.toEntity(isFavorite = false))
                }
                emit(DeathsResource.Success(data.map { entry ->
                    entry.copy(isFavorite = false)
                }))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(DeathsResource.Error("Greška: code=${response.code()}, message=${response.message()}, errorBody=$errorBody"))
            }
        } catch (e: Exception) {
            val local = dao.getAll().map { it.toDomain() }
            if (local.isNotEmpty()) {
                emit(DeathsResource.Success(local))
                emit(DeathsResource.Error("Nema interneta. Prikazani su poslednji sačuvani podaci."))
            } else {
                emit(DeathsResource.Error("Greška: ${e.localizedMessage}"))
            }
        }
    }
}

private fun DeathsEntry.uniqueKey() = "${entity}_${municipality}_${year}_${month}"
private fun DeathsEntity.uniqueKey() = "${entity}_${municipality}_${year}_${month}"

private fun DeathsEntity.toDomain() = DeathsEntry(
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

private fun DeathsEntry.toEntity(isFavorite: Boolean = false) = DeathsEntity(
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