package com.example.web_api_project.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.web_api_project.ui.theme.Primary
import com.example.web_api_project.ui.theme.Secondary
import com.example.web_api_project.ui.theme.Accent
import com.example.web_api_project.ui.theme.Tertiary
import androidx.compose.ui.draw.clip
//import androidx.compose.ui.Modifier.size

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Početna", Icons.Filled.Home)
    object Favorites : BottomNavItem("favorites", "Favoriti", Icons.Filled.Favorite)
    object Profile : BottomNavItem("profile", "Profil", Icons.Filled.Person)
    object Settings : BottomNavItem("settings", "Podešavanja", Icons.Filled.Settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Favorites,
    BottomNavItem.Profile,
    BottomNavItem.Settings
)

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Primary,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(MaterialTheme.shapes.large)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Accent else Color.White,
                        modifier = Modifier
                            .size(if (selected) 30.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (selected) Accent else Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Tertiary,
                    selectedIconColor = Accent,
                    selectedTextColor = Accent,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
} 