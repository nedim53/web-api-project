package com.example.web_api_project.ui.screen.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit, viewModel: OnboardingViewModel = viewModel()) {
    var entity by remember { mutableStateOf("FBiH") }
    var municipality by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(2024) }
    var showEntityMenu by remember { mutableStateOf(false) }
    val entityOptions = listOf("FBiH", "RS", "Brčko")

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dobrodošli!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("Odaberite svoje preferencije za prikaz podataka.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            ExposedDropdownMenuBox(
                expanded = showEntityMenu,
                onExpandedChange = { showEntityMenu = !showEntityMenu }
            ) {
                OutlinedTextField(
                    value = entity,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Entitet") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showEntityMenu) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = showEntityMenu,
                    onDismissRequest = { showEntityMenu = false }
                ) {
                    entityOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                entity = it
                                showEntityMenu = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = municipality,
                onValueChange = { municipality = it },
                label = { Text("Opština") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = year.toString(),
                onValueChange = { it.toIntOrNull()?.let { y -> year = y } },
                label = { Text("Godina") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.savePreferences(entity, municipality, year)
                    onFinish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nastavi")
            }
        }
    }
} 