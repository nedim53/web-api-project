package com.example.web_api_project.data.remote.deaths

data class DeathsRequest(
    val year: String,
    val month: Int?,
    val entity: String,
    val canton: String? = null,
    val municipality: String? = null,
    val updateDate: String? = null
)

data class DeathsResponse(
    val errors: List<String>,
    val result: List<DeathsEntry>
)

data class DeathsEntry(
    val id: Int = 0,
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