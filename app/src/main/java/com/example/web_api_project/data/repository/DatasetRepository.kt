package com.example.web_api_project.data.repository

import com.example.web_api_project.data.remote.api.DatasetApi
import com.example.web_api_project.data.local.dao.DatasetDao
import com.example.web_api_project.domain.model.Dataset
import com.example.web_api_project.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatasetRepository(
    private val api: DatasetApi,
    private val dao: DatasetDao
) {
    fun getDatasets(forceRefresh: Boolean): Flow<Resource<List<Dataset>>> = flow {
        emit(Resource.Loading())
        try {
            val localData = dao.getAll().map { it.toDomain() }
            emit(Resource.Success(localData))
            if (forceRefresh) {
                // ZAMIJENI "your_resource_id_here" sa stvarnim resource_id iz ODP BiH
                // Na primjer: "12345678-1234-1234-1234-123456789012"
                val response = api.getDatasets("your_resource_id_here")
                val datasets = response.result.records.map { it.toDomain() }
                dao.insertAll(datasets.map { it.toEntity() })
                emit(Resource.Success(datasets))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Greška: ${e.localizedMessage}"))
        }
    }
}

private fun Dataset.toEntity() = com.example.web_api_project.data.local.entity.DatasetEntity(
    id = id,
    naziv = naziv,
    vrijednost = vrijednost,
    kategorija = kategorija
)

private fun com.example.web_api_project.data.local.entity.DatasetEntity.toDomain() = Dataset(
    id = id,
    naziv = naziv,
    vrijednost = vrijednost,
    kategorija = kategorija
)

private fun com.example.web_api_project.data.remote.dto.DatasetDto.toDomain() = Dataset(
    id = id,
    naziv = naziv,
    vrijednost = vrijednost,
    kategorija = kategorija
) 