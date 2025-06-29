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
import com.example.web_api_project.ui.screen.details.DeathsDetailsScreen
import com.example.web_api_project.ui.screen.login.LoginScreen
import com.example.web_api_project.ui.screen.register.RegisterScreen
import com.example.web_api_project.ui.screen.favorites.FavoritesScreen
import com.example.web_api_project.ui.screen.profile.ProfileScreen
import com.example.web_api_project.ui.screen.home.NewbornListScreen
import com.example.web_api_project.ui.screen.onboarding.OnboardingScreen
import com.example.web_api_project.ui.UserSessionViewModel

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController(), userSessionViewModel: UserSessionViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userSessionViewModel) }
        composable("register") { RegisterScreen(navController) }
        composable("onboarding") { 
            OnboardingScreen(onFinish = { 
                val previousRoute = navController.previousBackStackEntry?.destination?.route
                if (previousRoute == "profile") {
                    navController.popBackStack()
                } else {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }) 
        }
        composable("splash") { SplashScreen(navController) }
        composable("home") { NewbornListScreen(navController, userSessionViewModel) }
        composable("favorites") { FavoritesScreen(navController, userSessionViewModel) }
        composable("profile") { ProfileScreen(navController, userSessionViewModel) }
        composable(
            "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments!!.getInt("id")
            DetailsScreen(
                navController = navController,
                newbornId = id,
                userSessionViewModel = userSessionViewModel
            )
        }
        composable(
            "deaths_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStack ->
            val id = backStack.arguments!!.getInt("id")
            DeathsDetailsScreen(
                navController = navController,
                deathsId = id,
                userSessionViewModel = userSessionViewModel
            )
        }
    }
} 