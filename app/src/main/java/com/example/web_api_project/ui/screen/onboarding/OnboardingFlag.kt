package com.example.web_api_project.ui.screen.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import com.example.web_api_project.utils.dataStore

object OnboardingFlag {
    private val ONBOARDING_DONE_KEY = booleanPreferencesKey("onboarding_done")

    suspend fun isOnboardingDone(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[ONBOARDING_DONE_KEY] ?: false
    }

    suspend fun setOnboardingDone(context: Context, done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_DONE_KEY] = done
        }
    }
} 