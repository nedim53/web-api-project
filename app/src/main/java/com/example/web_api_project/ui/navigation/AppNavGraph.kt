package com.example.web_api_project.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.web_api_project.ui.screen.splash.SplashScreen
import com.example.web_api_project.ui.screen.home.HomeScreen
import com.example.web_api_project.ui.screen.details.DetailsScreen
import com.example.web_api_project.ui.screen.login.LoginScreen
import com.example.web_api_project.ui.screen.register.RegisterScreen
import com.example.web_api_project.ui.screen.favorites.FavoritesScreen
import com.example.web_api_project.ui.screen.profile.ProfileScreen
import com.example.web_api_project.ui.screen.settings.SettingsScreen
import com.example.web_api_project.ui.screen.home.NewbornListScreen
import com.example.web_api_project.ui.UserSessionViewModel

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController(), userSessionViewModel: UserSessionViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userSessionViewModel) }
        composable("register") { RegisterScreen(navController) }
        composable("splash") { SplashScreen(navController) }
        composable("home") { NewbornListScreen(navController) }
        composable("favorites") { FavoritesScreen(navController) }
        composable("profile") { ProfileScreen(navController, userSessionViewModel) }
        composable("settings") { SettingsScreen() }
        composable(
            "details/{entity}/{canton}/{municipality}/{institution}/{year}/{month}/{dateUpdate}/{maleTotal}/{femaleTotal}/{total}",
            arguments = listOf(
                navArgument("entity") { type = NavType.StringType },
                navArgument("canton") { type = NavType.StringType },
                navArgument("municipality") { type = NavType.StringType },
                navArgument("institution") { type = NavType.StringType },
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("dateUpdate") { type = NavType.StringType },
                navArgument("maleTotal") { type = NavType.IntType },
                navArgument("femaleTotal") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStack ->
            val args = backStack.arguments!!
            val context = androidx.compose.ui.platform.LocalContext.current
            val newborn = com.example.web_api_project.data.local.entity.NewbornEntity(
                entity = args.getString("entity") ?: "",
                canton = args.getString("canton"),
                municipality = args.getString("municipality"),
                institution = args.getString("institution"),
                year = args.getInt("year"),
                month = args.getInt("month"),
                dateUpdate = args.getString("dateUpdate") ?: "",
                maleTotal = args.getInt("maleTotal"),
                femaleTotal = args.getInt("femaleTotal"),
                total = args.getInt("total")
            )
            val repository = com.example.web_api_project.data.repository.NewbornRepository(
                api = com.example.web_api_project.data.remote.newborn.NewbornRetrofitClient.getClient(),
                dao = com.example.web_api_project.data.local.DatabaseService.getDatabase(context).newbornDao()
            )
            DetailsScreen(
                navController = navController,
                newborn = newborn,
                repository = repository
            )
        }
        // favorites, settings...
    }
} 