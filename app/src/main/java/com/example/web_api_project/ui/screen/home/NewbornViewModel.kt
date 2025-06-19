package com.example.web_api_project.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.data.remote.newborn.NewbornRetrofitClient
import com.example.web_api_project.data.repository.NewbornRepository
import com.example.web_api_project.data.repository.NewbornResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class NewbornViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewbornRepository(
        api = NewbornRetrofitClient.getClient(),
        dao = DatabaseService.getDatabase(application).newbornDao()
    )

    private val _state = MutableStateFlow<NewbornResource>(NewbornResource.Loading)
    val state: StateFlow<NewbornResource> = _state.asStateFlow()

    val favorites: StateFlow<List<com.example.web_api_project.data.remote.newborn.NewbornEntry>> =
        repository.observeFavorites().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun loadData(token: String, year: Int, entity: String) {
        viewModelScope.launch {
            repository.getNewborns(token, year, entity).collect {
                _state.value = it
            }
        }
    }

    fun toggleFavorite(id: Int, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, isFav)
        }
    }
} 