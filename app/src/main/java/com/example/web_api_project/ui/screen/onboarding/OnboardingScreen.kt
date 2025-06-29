package com.example.web_api_project.ui.screen.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.first
import com.example.web_api_project.utils.dataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit, viewModel: OnboardingViewModel = viewModel()) {
    val context = LocalContext.current
    var entity by remember { mutableStateOf("FBiH") }
    var municipality by remember { mutableStateOf("") }
    val municipalityOptions = listOf("GORAŽDE", "SARAJEVO", "BANJA LUKA", "TUZLA", "ZENICA", "MOSTAR", "BIHAĆ", "BRČKO", "PRIJEDOR", "DOBOJ")
    var showMunicipalityMenu by remember { mutableStateOf(false) }
    var year by remember { mutableStateOf(2024) }
    var showEntityMenu by remember { mutableStateOf(false) }
    val entityOptions = listOf("FBiH", "RS", "Brčko")
    var datasetType by remember { mutableStateOf("Novorođeni") }
    val datasetOptions = listOf("Novorođeni", "Izvještaji o smrtima")
    var showDatasetMenu by remember { mutableStateOf(false) }

    // Učitaj postojeće preferencije ako postoje
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        val savedEntity = prefs[stringPreferencesKey("entity")]
        val savedMunicipality = prefs[stringPreferencesKey("municipality")]
        val savedYear = prefs[intPreferencesKey("year")]
        val savedDatasetType = prefs[stringPreferencesKey("dataset_type")]
        
        if (savedEntity != null) entity = savedEntity
        if (savedMunicipality != null) municipality = savedMunicipality
        if (savedYear != null) year = savedYear
        if (savedDatasetType != null) datasetType = savedDatasetType
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Postavke podataka", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("Odaberite kategoriju podataka koju želite da pratite.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            
            // Kategorija podataka
            ExposedDropdownMenuBox(
                expanded = showDatasetMenu,
                onExpandedChange = { showDatasetMenu = !showDatasetMenu }
            ) {
                OutlinedTextField(
                    value = datasetType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategorija podataka") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDatasetMenu) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = showDatasetMenu,
                    onDismissRequest = { showDatasetMenu = false }
                ) {
                    datasetOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                datasetType = it
                                showDatasetMenu = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            
            // Entitet
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
            
            // Opština
            Box {
                OutlinedTextField(
                    value = municipality,
                    onValueChange = {
                        municipality = it
                        showMunicipalityMenu = it.isNotBlank()
                    },
                    label = { Text("Opština (opciono)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = showMunicipalityMenu && municipality.isNotBlank(),
                    onDismissRequest = { showMunicipalityMenu = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    municipalityOptions.filter { it.contains(municipality, ignoreCase = true) }
                        .forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    municipality = it
                                    showMunicipalityMenu = false
                                }
                            )
                        }
                }
            }
            Spacer(Modifier.height(16.dp))
            
            // Godina
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
                    viewModel.saveDatasetType(datasetType)
                    onFinish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sačuvaj")
            }
        }
    }
} 