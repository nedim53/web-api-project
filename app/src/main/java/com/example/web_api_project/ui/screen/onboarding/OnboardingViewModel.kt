package com.example.web_api_project.ui.screen.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import com.example.web_api_project.utils.dataStore

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = application.dataStore

    private val _entity = MutableStateFlow("")
    val entity: StateFlow<String> = _entity.asStateFlow()
    private val _municipality = MutableStateFlow("")
    val municipality: StateFlow<String> = _municipality.asStateFlow()
    private val _year = MutableStateFlow(2024)
    val year: StateFlow<Int> = _year.asStateFlow()

    companion object {
        val ENTITY_KEY = stringPreferencesKey("entity")
        val MUNICIPALITY_KEY = stringPreferencesKey("municipality")
        val YEAR_KEY = intPreferencesKey("year")
    }

    fun savePreferences(entity: String, municipality: String, year: Int) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[ENTITY_KEY] = entity
                prefs[MUNICIPALITY_KEY] = municipality
                prefs[YEAR_KEY] = year
            }
            _entity.value = entity
            _municipality.value = municipality
            _year.value = year
        }
    }

    fun loadPreferences() {
        viewModelScope.launch {
            val prefs = dataStore.data.first()
            _entity.value = prefs[ENTITY_KEY] ?: ""
            _municipality.value = prefs[MUNICIPALITY_KEY] ?: ""
            _year.value = prefs[YEAR_KEY] ?: 2024
        }
    }

    fun saveDatasetType(datasetType: String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[stringPreferencesKey("dataset_type")] = datasetType
            }
        }
    }
} 