package com.example.web_api_project.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.domain.model.Dataset
import com.example.web_api_project.utils.Resource
import com.example.web_api_project.ui.theme.WebApiProjectTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()
    WebApiProjectTheme {
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ODP BiH - Podaci", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (state) {
                    is Resource.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                    is Resource.Success -> LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items((state as Resource.Success<List<Dataset>>).data) { item ->
                            DatasetCard(item)
                        }
                    }
                    is Resource.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Greška: ${(state as Resource.Error).message}", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun DatasetCard(dataset: Dataset) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(dataset.naziv ?: "Bez naziva", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(dataset.vrijednost ?: "", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(2.dp))
            Text(dataset.kategorija ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        }
    }
} 