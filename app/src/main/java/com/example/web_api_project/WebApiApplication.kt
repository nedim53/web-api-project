package com.example.web_api_project

import android.app.Application

class WebApiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Ovdje možeš inicijalizirati dependency injection, logging, itd.
    }
} 