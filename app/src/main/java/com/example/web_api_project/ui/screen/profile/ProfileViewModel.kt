package com.example.web_api_project.ui.screen.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.data.local.entity.UserEntity
import com.example.web_api_project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = DatabaseService.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    fun loadUser(email: String) {
        viewModelScope.launch {
            _user.value = repository.getUserByEmail(email)
        }
    }
} 