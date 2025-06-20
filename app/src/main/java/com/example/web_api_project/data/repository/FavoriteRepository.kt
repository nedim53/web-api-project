package com.example.web_api_project.data.repository

import com.example.web_api_project.data.local.dao.FavoriteDao
import com.example.web_api_project.data.local.entity.FavoriteEntity

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    suspend fun addFavorite(userId: Int, newbornId: Int) {
        favoriteDao.insertFavorite(FavoriteEntity(userId = userId, newbornId = newbornId))
    }

    suspend fun removeFavorite(userId: Int, newbornId: Int) {
        favoriteDao.removeFavorite(userId, newbornId)
    }

    suspend fun getFavoritesForUser(userId: Int): List<FavoriteEntity> {
        return favoriteDao.getFavoritesForUser(userId)
    }

    suspend fun isFavorite(userId: Int, newbornId: Int): Boolean {
        return favoriteDao.isFavorite(userId, newbornId)
    }
} 