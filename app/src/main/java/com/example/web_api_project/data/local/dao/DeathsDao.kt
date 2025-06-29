package com.example.web_api_project.data.local.dao

import androidx.room.*
import com.example.web_api_project.data.local.entity.DeathsEntity

@Dao
interface DeathsDao {
    @Query("SELECT * FROM deaths ORDER BY year DESC, month DESC")
    suspend fun getAll(): List<DeathsEntity>

    @Query("SELECT * FROM deaths WHERE isFavorite = 1 ORDER BY year DESC, month DESC")
    suspend fun getFavorites(): List<DeathsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<DeathsEntity>)

    @Update
    suspend fun update(deaths: DeathsEntity)

    @Query("UPDATE deaths SET isFavorite = 1 WHERE id = :id")
    suspend fun setFavorite(id: Int)

    @Query("UPDATE deaths SET isFavorite = 0 WHERE id = :id")
    suspend fun unsetFavorite(id: Int)

    @Query("DELETE FROM deaths")
    suspend fun clearAll()

    @Query("SELECT * FROM deaths WHERE isFavorite = 1 ORDER BY year DESC, month DESC")
    fun observeFavorites(): kotlinx.coroutines.flow.Flow<List<DeathsEntity>>

    @Query("UPDATE deaths SET isFavorite = :isFavorite WHERE entity = :entity AND municipality = :municipality AND year = :year AND month = :month")
    suspend fun setFavoriteByFields(entity: String, municipality: String?, year: Int, month: Int, isFavorite: Boolean)

    @Query("SELECT * FROM deaths WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): DeathsEntity?

    @Query("SELECT * FROM deaths WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<DeathsEntity>

    @Transaction
    suspend fun upsert(deaths: DeathsEntity) {
        val existing = getByUniqueKey(deaths.entity, deaths.municipality, deaths.year, deaths.month)
        if (existing != null) {
            update(deaths.copy(id = existing.id))
        } else {
            insert(deaths)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(deaths: DeathsEntity)

    @Query("SELECT * FROM deaths WHERE entity = :entity AND municipality IS :municipality AND year = :year AND month = :month LIMIT 1")
    suspend fun getByUniqueKey(entity: String, municipality: String?, year: Int, month: Int): DeathsEntity?
} 