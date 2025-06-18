package com.example.web_api_project.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.web_api_project.data.repository.DatasetRepository
import com.example.web_api_project.data.remote.RetrofitService
import com.example.web_api_project.data.local.DatabaseService
import com.example.web_api_project.domain.model.Dataset
import com.example.web_api_project.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = DatasetRepository(
        api = RetrofitService.datasetApi,
        dao = DatabaseService.getDatabase(application).datasetDao()
    )

    private val _state = MutableStateFlow<Resource<List<Dataset>>>(Resource.Loading())
    val state: StateFlow<Resource<List<Dataset>>> = _state.asStateFlow()

    init {
        getDatasets()
    }

    fun getDatasets(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            repository.getDatasets(forceRefresh).collect {
                _state.value = it
            }
        }
    }
} 