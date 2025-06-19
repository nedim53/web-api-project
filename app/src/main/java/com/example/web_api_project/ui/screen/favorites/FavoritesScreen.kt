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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, viewModel: NewbornViewModel = viewModel()) {
    val favorites by viewModel.favorites.collectAsState()

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
                        Column(Modifier.padding(12.dp)) {
                            Text(text = "${entry.year}/${entry.month} - ${entry.municipality}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Ukupno: ${entry.total} (M: ${entry.maleTotal}, Ž: ${entry.femaleTotal})")
                            Text(text = "Ažurirano: ${entry.dateUpdate}")
                        }
                    }
                }
            }
        }
    }
} 