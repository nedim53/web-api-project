package com.example.web_api_project.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.data.remote.deaths.DeathsRetrofitClient
import com.example.web_api_project.data.repository.DeathsRepository
import com.example.web_api_project.data.repository.DeathsResource
import com.example.web_api_project.data.repository.FavoriteRepository
import com.example.web_api_project.data.local.dao.UserDao
import com.example.web_api_project.data.local.dao.FavoriteDao
import com.example.web_api_project.data.local.entity.FavoriteEntity
import com.example.web_api_project.data.local.entity.UserEntity
import com.example.web_api_project.data.local.entity.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.web_api_project.data.remote.deaths.DeathsEntry
import com.example.web_api_project.data.local.entity.DeathsEntity

class DeathsViewModel(application: Application) : AndroidViewModel(application) {
    val db = DatabaseService.getDatabase(application)
    private val repository = DeathsRepository(
        api = DeathsRetrofitClient.getClient(),
        dao = db.deathsDao()
    )
    private val favoriteRepository = FavoriteRepository(db.favoriteDao())
    private val userDao = db.userDao()

    private val _state = MutableStateFlow<DeathsResource>(DeathsResource.Loading)
    val state: StateFlow<DeathsResource> = _state.asStateFlow()

    private val _favorites = MutableStateFlow<List<DeathsEntry>>(emptyList())
    val favorites: StateFlow<List<DeathsEntry>> = _favorites.asStateFlow()

    private var currentUserId: Int? = null

    private val deathsKeyToId = mutableMapOf<String, Int>()

    private val _favoriteIds = MutableStateFlow<List<Int>>(emptyList())
    val favoriteIds: StateFlow<List<Int>> = _favoriteIds.asStateFlow()

    suspend fun setCurrentUserByEmail(email: String) {
        println("setCurrentUserByEmail: email=$email")
        val user = userDao.getUserByEmail(email)
        println("setCurrentUserByEmail: user=$user")
        println("setCurrentUserByEmail: user?.id=${user?.id}")
        currentUserId = user?.id
        user?.id?.let { loadFavoritesForUser(it) }
    }

    private suspend fun loadFavoritesForUser(userId: Int) {
        val favoriteEntities = favoriteRepository.getFavoritesForUser(userId)
        _favoriteIds.value = favoriteEntities
            .filter { it.dataType == "deaths" && it.deathsId != null }
            .map { it.deathsId!! }
    }

    fun loadData(token: String, year: Int, entity: String, municipality: String? = null) {
        viewModelScope.launch {
            repository.getDeaths(token, year, entity, municipality).collect {
                _state.value = it
                val deaths = db.deathsDao().getAll()
                deathsKeyToId.clear()
                deaths.forEach { deathsKeyToId[deathsEntityUniqueKey(it)] = it.id }
                _state.value = DeathsResource.Success(deaths.map { it.toDomain() })
                currentUserId?.let { loadFavoritesForUser(it) }
            }
        }
    }

    fun toggleFavorite(entry: DeathsEntry) {
        viewModelScope.launch {
            try {
                val userId = currentUserId ?: return@launch
                val roomId = entry.id
                if (roomId == 0) return@launch
                val isFav = favoriteRepository.isDeathsFavorite(userId, roomId)
                if (isFav) {
                    favoriteRepository.removeDeathsFavorite(userId, roomId)
                } else {
                    favoriteRepository.addDeathsFavorite(userId, roomId)
                }
                loadFavoritesForUser(userId)
            } catch (e: Exception) {
                println("toggleFavorite error: ${e.message}")
            }
        }
    }

    fun refreshUserAndFavorites(email: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            currentUserId = user?.id
            user?.id?.let { loadFavoritesForUser(it) }
        }
    }

    private fun deathsEntityUniqueKey(entity: DeathsEntity) = "${entity.entity}_${entity.municipality}_${entity.year}_${entity.month}"
    private fun deathsEntryUniqueKey(entry: DeathsEntry) = "${entry.entity}_${entry.municipality}_${entry.year}_${entry.month}"

    suspend fun getFavoriteEntries(): List<DeathsEntry> {
        val ids = favoriteIds.value
        if (ids.isEmpty()) return emptyList()
        val entities = db.deathsDao().getByIds(ids)
        return entities.map { it.toDomain() }
    }
} 