package com.example.web_api_project.data.local.dao

import androidx.room.*
import com.example.web_api_project.data.local.entity.NewbornEntity

@Dao
interface NewbornDao {
    @Query("SELECT * FROM newborn ORDER BY year DESC, month DESC")
    suspend fun getAll(): List<NewbornEntity>

    @Query("SELECT * FROM newborn WHERE isFavorite = 1 ORDER BY year DESC, month DESC")
    suspend fun getFavorites(): List<NewbornEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<NewbornEntity>)

    @Update
    suspend fun update(newborn: NewbornEntity)

    @Query("UPDATE newborn SET isFavorite = 1 WHERE id = :id")
    suspend fun setFavorite(id: Int)

    @Query("UPDATE newborn SET isFavorite = 0 WHERE id = :id")
    suspend fun unsetFavorite(id: Int)

    @Query("DELETE FROM newborn")
    suspend fun clearAll()

    @Query("SELECT * FROM newborn WHERE isFavorite = 1 ORDER BY year DESC, month DESC")
    fun observeFavorites(): kotlinx.coroutines.flow.Flow<List<NewbornEntity>>

    @Query("UPDATE newborn SET isFavorite = :isFavorite WHERE entity = :entity AND municipality = :municipality AND year = :year AND month = :month")
    suspend fun setFavoriteByFields(entity: String, municipality: String?, year: Int, month: Int, isFavorite: Boolean)
} 