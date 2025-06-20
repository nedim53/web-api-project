package com.example.web_api_project.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.data.remote.newborn.NewbornRetrofitClient
import com.example.web_api_project.data.repository.NewbornRepository
import com.example.web_api_project.data.repository.NewbornResource
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
import com.example.web_api_project.data.remote.newborn.NewbornEntry
import com.example.web_api_project.data.local.entity.NewbornEntity

class NewbornViewModel(application: Application) : AndroidViewModel(application) {
    val db = DatabaseService.getDatabase(application)
    private val repository = NewbornRepository(
        api = NewbornRetrofitClient.getClient(),
        dao = db.newbornDao()
    )
    private val favoriteRepository = FavoriteRepository(db.favoriteDao())
    private val userDao = db.userDao()

    private val _state = MutableStateFlow<NewbornResource>(NewbornResource.Loading)
    val state: StateFlow<NewbornResource> = _state.asStateFlow()

    private val _favorites = MutableStateFlow<List<com.example.web_api_project.data.remote.newborn.NewbornEntry>>(emptyList())
    val favorites: StateFlow<List<com.example.web_api_project.data.remote.newborn.NewbornEntry>> = _favorites.asStateFlow()

    private var currentUserId: Int? = null

    private val newbornKeyToId = mutableMapOf<String, Int>()

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
        _favoriteIds.value = favoriteEntities.map { it.newbornId }
    }

    fun loadData(token: String, year: Int, entity: String) {
        viewModelScope.launch {
            repository.getNewborns(token, year, entity).collect {
                _state.value = it
                val newborns = db.newbornDao().getAll()
                newbornKeyToId.clear()
                newborns.forEach { newbornKeyToId[newbornEntityUniqueKey(it)] = it.id }
                _state.value = com.example.web_api_project.data.repository.NewbornResource.Success(newborns.map { it.toDomain() })
                currentUserId?.let { loadFavoritesForUser(it) }
            }
        }
    }

    fun toggleFavorite(entry: NewbornEntry) {
        viewModelScope.launch {
            try {
                val userId = currentUserId ?: return@launch
                val roomId = entry.id
                if (roomId == 0) return@launch
                val isFav = favoriteRepository.isFavorite(userId, roomId)
                if (isFav) {
                    favoriteRepository.removeFavorite(userId, roomId)
                } else {
                    favoriteRepository.addFavorite(userId, roomId)
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

    private fun newbornEntityUniqueKey(entity: NewbornEntity) = "${'$'}{entity.entity}_${'$'}{entity.municipality}_${'$'}{entity.year}_${'$'}{entity.month}"
    private fun newbornEntryUniqueKey(entry: NewbornEntry) = "${'$'}{entry.entity}_${'$'}{entry.municipality}_${'$'}{entry.year}_${'$'}{entry.month}"

    suspend fun getFavoriteEntries(): List<NewbornEntry> {
        val ids = favoriteIds.value
        if (ids.isEmpty()) return emptyList()
        val entities = db.newbornDao().getByIds(ids)
        return entities.map { it.toDomain() }
    }
} 