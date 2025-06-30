package com.example.web_api_project.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseService {
    
    @Volatile
    private var INSTANCE: AppDatabase? = null
    
    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
            .addMigrations(MIGRATION_6_7)
            .build()
            INSTANCE = instance
            instance
        }
    }

    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE favorites_new (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "userId INTEGER NOT NULL, " +
                "newbornId INTEGER, " +
                "deathsId INTEGER, " +
                "dataType TEXT NOT NULL)"
            )
            
            database.execSQL(
                "INSERT INTO favorites_new (id, userId, newbornId, deathsId, dataType) " +
                "SELECT id, userId, newbornId, NULL, 'newborn' FROM favorites"
            )
            
            database.execSQL("DROP TABLE favorites")
            
            database.execSQL("ALTER TABLE favorites_new RENAME TO favorites")
        }
    }
} 