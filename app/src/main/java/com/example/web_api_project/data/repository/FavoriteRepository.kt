package com.example.web_api_project.data.repository

import com.example.web_api_project.data.local.dao.FavoriteDao
import com.example.web_api_project.data.local.entity.FavoriteEntity

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    suspend fun addNewbornFavorite(userId: Int, newbornId: Int) {
        favoriteDao.insertFavorite(FavoriteEntity(userId = userId, newbornId = newbornId, dataType = "newborn"))
    }

    suspend fun addDeathsFavorite(userId: Int, deathsId: Int) {
        favoriteDao.insertFavorite(FavoriteEntity(userId = userId, deathsId = deathsId, dataType = "deaths"))
    }

    suspend fun removeNewbornFavorite(userId: Int, newbornId: Int) {
        favoriteDao.removeNewbornFavorite(userId, newbornId)
    }

    suspend fun removeDeathsFavorite(userId: Int, deathsId: Int) {
        favoriteDao.removeDeathsFavorite(userId, deathsId)
    }

    suspend fun getFavoritesForUser(userId: Int): List<FavoriteEntity> {
        return favoriteDao.getFavoritesForUser(userId)
    }

    suspend fun isNewbornFavorite(userId: Int, newbornId: Int): Boolean {
        return favoriteDao.isNewbornFavorite(userId, newbornId)
    }

    suspend fun isDeathsFavorite(userId: Int, deathsId: Int): Boolean {
        return favoriteDao.isDeathsFavorite(userId, deathsId)
    }

    // Legacy methods for backward compatibility
    suspend fun addFavorite(userId: Int, newbornId: Int) {
        addNewbornFavorite(userId, newbornId)
    }

    suspend fun removeFavorite(userId: Int, newbornId: Int) {
        removeNewbornFavorite(userId, newbornId)
    }

    suspend fun isFavorite(userId: Int, newbornId: Int): Boolean {
        return isNewbornFavorite(userId, newbornId)
    }
} 