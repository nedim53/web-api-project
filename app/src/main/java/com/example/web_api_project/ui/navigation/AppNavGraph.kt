package com.example.web_api_project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.web_api_project.ui.screen.splash.SplashScreen
import com.example.web_api_project.ui.screen.home.HomeScreen
import com.example.web_api_project.ui.screen.details.DetailsScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen() }
        composable("details/{id}") { backStack ->
            val id = backStack.arguments?.getString("id") ?: ""
            DetailsScreen(id)
        }
        // favorites, settings...
    }
} 