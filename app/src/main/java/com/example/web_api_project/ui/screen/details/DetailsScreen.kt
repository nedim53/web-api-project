package com.example.web_api_project.ui.screen.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailsScreen(id: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji - $id") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Detalji za ID: $id", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ovdje će biti prikazani detaljni podaci...", style = MaterialTheme.typography.bodyMedium)
        }
    }
} 