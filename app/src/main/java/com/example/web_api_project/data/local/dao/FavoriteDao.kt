package com.example.web_api_project.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.web_api_project.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    suspend fun getFavoritesForUser(userId: Int): List<FavoriteEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND newbornId = :newbornId AND dataType = 'newborn')")
    suspend fun isNewbornFavorite(userId: Int, newbornId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND deathsId = :deathsId AND dataType = 'deaths')")
    suspend fun isDeathsFavorite(userId: Int, deathsId: Int): Boolean

    @Query("DELETE FROM favorites WHERE userId = :userId AND newbornId = :newbornId AND dataType = 'newborn'")
    suspend fun removeNewbornFavorite(userId: Int, newbornId: Int)

    @Query("DELETE FROM favorites WHERE userId = :userId AND deathsId = :deathsId AND dataType = 'deaths'")
    suspend fun removeDeathsFavorite(userId: Int, deathsId: Int)

    // Legacy methods for backward compatibility
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND newbornId = :newbornId)")
    suspend fun isFavorite(userId: Int, newbornId: Int): Boolean

    @Query("DELETE FROM favorites WHERE userId = :userId AND newbornId = :newbornId")
    suspend fun removeFavorite(userId: Int, newbornId: Int)
} 