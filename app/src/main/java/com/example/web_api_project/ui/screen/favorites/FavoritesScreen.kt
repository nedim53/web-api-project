package com.example.web_api_project.ui.screen.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.screen.home.NewbornViewModel
import androidx.navigation.NavController
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, userSessionViewModel: UserSessionViewModel, viewModel: NewbornViewModel = viewModel()) {
    val email by userSessionViewModel.userEmail.collectAsState()
    val emailValue = email
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val state by viewModel.state.collectAsState()
    var favorites by remember { mutableStateOf<List<com.example.web_api_project.data.remote.newborn.NewbornEntry>>(emptyList()) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(emailValue) {
        println("LaunchedEffect (FavoritesScreen): email=$emailValue")
        if (emailValue == null) {
            userSessionViewModel.login("test@email.com")
            println("LaunchedEffect: set test email")
        } else {
            viewModel.refreshUserAndFavorites(emailValue)
        }
    }
    LaunchedEffect(favoriteIds) {
        scope.launch {
            favorites = viewModel.getFavoriteEntries()
        }
    }
    val data = if (state is com.example.web_api_project.data.repository.NewbornResource.Success) (state as com.example.web_api_project.data.repository.NewbornResource.Success).data else emptyList()
    val favoritesFromData = data.filter { favoriteIds.contains(it.id) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favoriti") })
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nema sačuvanih podataka.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(favorites) { entry ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "details/${entry.entity}/${entry.canton}/${entry.municipality}/${entry.institution}/${entry.year}/${entry.month}/${entry.dateUpdate}/${entry.maleTotal}/${entry.femaleTotal}/${entry.total}"
                                )
                            }
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(text = "${entry.year}/${entry.month} - ${entry.municipality}", style = MaterialTheme.typography.titleMedium)
                                Text(text = "Ukupno: ${entry.total} (M: ${entry.maleTotal}, Ž: ${entry.femaleTotal})")
                                Text(text = "Ažurirano: ${entry.dateUpdate}")
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(entry) }) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Ukloni iz favorita",
                                    tint = Color.Yellow
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 