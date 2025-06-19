package com.example.web_api_project.data.repository

import com.example.web_api_project.data.local.dao.UserDao
import com.example.web_api_project.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun register(user: UserEntity): Result<Long> = try {
        val existing = userDao.getUserByEmail(user.email)
        if (existing != null) {
            Result.failure(Exception("Korisnik sa ovim emailom već postoji."))
        } else {
            Result.success(userDao.insert(user))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): UserEntity? =
        userDao.login(email, password)

    suspend fun getUserByEmail(email: String): UserEntity? =
        userDao.getUserByEmail(email)

    suspend fun update(user: UserEntity) = userDao.update(user)
} 