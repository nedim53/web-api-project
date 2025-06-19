package com.example.web_api_project.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.web_api_project.data.remote.newborn.NewbornEntry
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.web_api_project.utils.dataStore
import androidx.navigation.NavController
import com.example.web_api_project.data.repository.NewbornResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewbornListScreen(
    navController: NavController,
    viewModel: NewbornViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var year by remember { mutableStateOf(2024) }
    var entity by remember { mutableStateOf("FBiH") }
    var municipality by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUwMjY5OTE4LCJleHAiOjE3NTAzNTYzMTgsImlhdCI6MTc1MDI2OTkxOH0.P5Q6T786VSUZmanQDMKBN60wtjB6QT8dRyLGc-e_XROEaaMVrIdp8VweqIIZWIWlUkrB8chKxztxD7BIbi6A6w"
    var isRefreshing by remember { mutableStateOf(false) }

    // Učitaj preferencije iz DataStore na početku
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        entity = prefs[stringPreferencesKey("entity")] ?: "FBiH"
        municipality = prefs[stringPreferencesKey("municipality")] ?: ""
        year = prefs[intPreferencesKey("year")] ?: 2024
        viewModel.loadData(token, year, entity)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = year.toString(),
                onValueChange = { it.toIntOrNull()?.let { y -> year = y } },
                label = { Text("Godina") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = entity,
                onValueChange = { entity = it },
                label = { Text("Entitet") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { viewModel.loadData(token, year, entity) }) {
                Text("Prikaži")
            }
        }
        Spacer(Modifier.height(16.dp))
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.loadData(token, year, entity)
            }
        ) {
            when (state) {
                is NewbornResource.Loading -> {
                    isRefreshing = false
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is NewbornResource.Success -> {
                    isRefreshing = false
                    val data = (state as NewbornResource.Success).data
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(data) { entry ->
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
                is NewbornResource.Error -> {
                    isRefreshing = false
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((state as NewbornResource.Error).message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
} 