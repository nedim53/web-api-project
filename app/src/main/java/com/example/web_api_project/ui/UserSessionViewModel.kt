package com.example.web_api_project.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionViewModel : ViewModel() {
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    fun login(email: String) {
        _userEmail.value = email
    }

    fun logout() {
        _userEmail.value = null
    }
} 