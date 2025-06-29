package com.example.web_api_project.ui.screen.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel
import com.example.web_api_project.ui.screen.home.NewbornViewModel
import com.example.web_api_project.ui.screen.home.DeathsViewModel
import com.example.web_api_project.data.remote.newborn.NewbornEntry
import com.example.web_api_project.data.remote.deaths.DeathsEntry
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, userSessionViewModel: UserSessionViewModel, newbornViewModel: NewbornViewModel = viewModel(), deathsViewModel: DeathsViewModel = viewModel()) {
    val email by userSessionViewModel.userEmail.collectAsState()
    val emailValue = email
    val newbornFavoriteIds by newbornViewModel.favoriteIds.collectAsState()
    val deathsFavoriteIds by deathsViewModel.favoriteIds.collectAsState()
    val newbornState by newbornViewModel.state.collectAsState()
    val deathsState by deathsViewModel.state.collectAsState()
    var newbornFavorites by remember { mutableStateOf<List<NewbornEntry>>(emptyList()) }
    var deathsFavorites by remember { mutableStateOf<List<DeathsEntry>>(emptyList()) }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(emailValue) {
        println("LaunchedEffect (FavoritesScreen): email=$emailValue")
        if (emailValue == null) {
            userSessionViewModel.login("test@email.com")
            println("LaunchedEffect: set test email")
        } else {
            newbornViewModel.refreshUserAndFavorites(emailValue)
            deathsViewModel.refreshUserAndFavorites(emailValue)
        }
    }
    
    LaunchedEffect(newbornFavoriteIds) {
        scope.launch {
            newbornFavorites = newbornViewModel.getFavoriteEntries()
        }
    }
    
    LaunchedEffect(deathsFavoriteIds) {
        scope.launch {
            deathsFavorites = deathsViewModel.getFavoriteEntries()
        }
    }
    
    val allFavorites = newbornFavorites + deathsFavorites

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favoriti") })
        }
    ) { padding ->
        if (allFavorites.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nema sačuvanih podataka.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(allFavorites) { entry ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (entry) {
                                    is NewbornEntry -> navController.navigate("details/${entry.id}")
                                    is DeathsEntry -> navController.navigate("deaths_details/${entry.id}")
                                }
                            }
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                when (entry) {
                                    is NewbornEntry -> {
                                        Text(text = "${entry.year}/${entry.month} - ${entry.municipality} (Novorođeni)", style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Ukupno: ${entry.total} (M: ${entry.maleTotal}, Ž: ${entry.femaleTotal})")
                                        Text(text = "Ažurirano: ${entry.dateUpdate}")
                                    }
                                    is DeathsEntry -> {
                                        Text(text = "${entry.year}/${entry.month} - ${entry.municipality} (Smrti)", style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Ukupno: ${entry.total} (M: ${entry.maleTotal}, Ž: ${entry.femaleTotal})")
                                        Text(text = "Ažurirano: ${entry.dateUpdate}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 