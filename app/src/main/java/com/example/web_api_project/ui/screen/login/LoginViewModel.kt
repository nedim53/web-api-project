package com.example.web_api_project.ui.screen.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiEvent {
    data class Success(val email: String) : LoginUiEvent()
    data class Error(val message: String) : LoginUiEvent()
    object Idle : LoginUiEvent()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = DatabaseService.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    private val _uiEvent = MutableStateFlow<LoginUiEvent>(LoginUiEvent.Idle)
    val uiEvent: StateFlow<LoginUiEvent> = _uiEvent.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            if (user != null) {
                _uiEvent.value = LoginUiEvent.Success(user.email)
            } else {
                _uiEvent.value = LoginUiEvent.Error("Pogrešan email ili lozinka.")
            }
        }
    }

    fun resetEvent() {
        _uiEvent.value = LoginUiEvent.Idle
    }
} 