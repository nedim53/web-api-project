package com.example.web_api_project.data.remote.dto

import com.squareup.moshi.Json

// OVO JE PRIMJER - prilagodi prema stvarnoj JSON strukturi iz API-ja
data class ApiResponse(
    @field:Json(name = "success") val success: Boolean,
    @field:Json(name = "result") val result: ResultDto
)

data class ResultDto(
    @field:Json(name = "records") val records: List<DatasetDto>
)

data class DatasetDto(
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "naziv") val naziv: String?,      // Prilagodi ime polja
    @field:Json(name = "vrijednost") val vrijednost: String?, // Prilagodi ime polja  
    @field:Json(name = "kategorija") val kategorija: String?  // Prilagodi ime polja
)

// PRIMJER kako da prilagodiš za različite datasetove:
// data class DatasetDto(
//     @field:Json(name = "_id") val id: String,
//     @field:Json(name = "naziv_firme") val naziv: String?,
//     @field:Json(name = "adresa") val vrijednost: String?,
//     @field:Json(name = "grad") val kategorija: String?
// ) 