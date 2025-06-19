package com.example.web_api_project.ui.screen.register

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

sealed class RegisterUiEvent {
    object Success : RegisterUiEvent()
    data class Error(val message: String) : RegisterUiEvent()
    object Idle : RegisterUiEvent()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = DatabaseService.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    private val _uiEvent = MutableStateFlow<RegisterUiEvent>(RegisterUiEvent.Idle)
    val uiEvent: StateFlow<RegisterUiEvent> = _uiEvent.asStateFlow()

    fun register(user: UserEntity) {
        viewModelScope.launch {
            val result = repository.register(user)
            if (result.isSuccess) {
                _uiEvent.value = RegisterUiEvent.Success
            } else {
                _uiEvent.value = RegisterUiEvent.Error(result.exceptionOrNull()?.message ?: "Greška pri registraciji")
            }
        }
    }

    fun resetEvent() {
        _uiEvent.value = RegisterUiEvent.Idle
    }
} 