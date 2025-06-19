package com.example.web_api_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.web_api_project.ui.navigation.AppNavGraph
import com.example.web_api_project.ui.navigation.BottomNavBar
import androidx.navigation.compose.rememberNavController
import com.example.web_api_project.ui.theme.WebApiTheme
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userSessionViewModel: UserSessionViewModel = viewModel()
            WebApiTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showBottomBarRoutes = listOf("home", "favorites", "profile", "settings")
                    val navBackStackEntry = navController.currentBackStackEntryAsState().value
                    val currentRoute = navBackStackEntry?.destination?.route
                    Scaffold(
                        bottomBar = {
                            if (currentRoute in showBottomBarRoutes) {
                                BottomNavBar(navController)
                            }
                        }
                    ) { padding ->
                        Box(Modifier.padding(padding)) {
                            AppNavGraph(navController, userSessionViewModel)
                        }
                    }
                }
            }
        }
    }
}