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
import com.example.web_api_project.data.remote.deaths.DeathsEntry
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.web_api_project.utils.dataStore
import androidx.navigation.NavController
import com.example.web_api_project.data.repository.DeathsResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.Color
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeathsListScreen(
    navController: NavController,
    userSessionViewModel: UserSessionViewModel,
    viewModel: DeathsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var year by remember { mutableStateOf(2024) }
    var entity by remember { mutableStateOf("FBiH") }
    var municipality by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMDk1IiwibmJmIjoxNzUwMjY5OTE4LCJleHAiOjE3NTAzNTYzMTgsImlhdCI6MTc1MDI2OTkxOH0.P5Q6T786VSUZmanQDMKBN60wtjB6QT8dRyLGc-e_XROEaaMVrIdp8VweqIIZWIWlUkrB8chKxztxD7BIbi6A6w"
    var isRefreshing by remember { mutableStateOf(false) }

    val email by userSessionViewModel.userEmail.collectAsState()
    val emailValue = email
    LaunchedEffect(emailValue) {
        if (emailValue == null) {
            userSessionViewModel.login("test@email.com")
        } else {
            viewModel.refreshUserAndFavorites(emailValue)
        }
    }

    // Učitaj preferencije iz DataStore na početku
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        entity = prefs[stringPreferencesKey("entity")] ?: "FBiH"
        municipality = prefs[stringPreferencesKey("municipality")] ?: ""
        year = prefs[intPreferencesKey("year")] ?: 2024
        viewModel.loadData(token, year, entity)
    }

    var searchText by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("Godina") }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Izvještaji o smrtima", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(Modifier.padding(padding)) {
            // Kontrole za filtriranje i sortiranje
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Pretraži opštinu") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                ExposedDropdownMenuBox(
                    expanded = sortMenuExpanded,
                    onExpandedChange = { sortMenuExpanded = !sortMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = sortOption,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sortiraj") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortMenuExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        listOf("Godina", "Ukupno", "Opština").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    sortOption = option
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    viewModel.loadData(token, year, entity)
                }
            ) {
                when (state) {
                    is DeathsResource.Loading -> {
                        isRefreshing = false
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    is DeathsResource.Success -> {
                        isRefreshing = false
                        var data = (state as DeathsResource.Success).data
                        // Lokalno filtriranje po opštini
                        data = if (searchText.isNotBlank()) data.filter { it.municipality?.contains(searchText, ignoreCase = true) == true } else data
                        // Sortiranje
                        data = when (sortOption) {
                            "Godina" -> data.sortedWith(compareByDescending<DeathsEntry> { it.year }.thenByDescending { it.month })
                            "Ukupno" -> data.sortedByDescending { it.total }
                            "Opština" -> data.sortedBy { it.municipality ?: "" }
                            else -> data
                        }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(data) { entry ->
                                val isFav = favoriteIds.contains(entry.id)
                                Card(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("deaths_details/${entry.id}")
                                        }
                                ) {
                                    Row(
                                        Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(Modifier.weight(1f)) {
                                            Text(text = "${entry.year}/${entry.month} - ${entry.municipality}", style = MaterialTheme.typography.titleMedium)
                                            Text(text = "Ukupno: ${entry.total} (M: ${entry.maleTotal}, Ž: ${entry.femaleTotal})")
                                            Text(text = "Ažurirano: ${entry.dateUpdate}")
                                        }
                                        IconButton(onClick = { viewModel.toggleFavorite(entry) }) {
                                            Icon(
                                                imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.Star,
                                                contentDescription = if (isFav) "Ukloni iz favorita" else "Dodaj u favorite",
                                                tint = if (isFav) Color.Yellow else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is DeathsResource.Error -> {
                        isRefreshing = false
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text((state as DeathsResource.Error).message, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
} 