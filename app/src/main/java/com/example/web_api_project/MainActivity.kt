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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.web_api_project.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userSessionViewModel: UserSessionViewModel = viewModel()
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            var autoLoginEmail by remember { mutableStateOf<String?>(null) }
            
            // Pratimo promene u userEmail
            val userEmail by userSessionViewModel.userEmail.collectAsState()
            
            LaunchedEffect(userEmail) {
                val context = this@MainActivity
                if (userEmail == null) {
                    // Proveri da li postoji email u DataStore za auto-login
                    val prefs = context.dataStore.data.first()
                    autoLoginEmail = prefs[androidx.datastore.preferences.core.stringPreferencesKey("last_email")]
                    autoLoginEmail?.let { userSessionViewModel.login(it) }
                }
            }
            
            WebApiTheme {
                val showBottomBarRoutes = listOf("home", "favorites", "profile")
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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