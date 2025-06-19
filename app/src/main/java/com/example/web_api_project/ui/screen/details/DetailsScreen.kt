package com.example.web_api_project.ui.screen.details

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.web_api_project.data.repository.NewbornRepository
import com.example.web_api_project.data.local.entity.NewbornEntity
import com.example.web_api_project.ui.theme.Primary
import com.example.web_api_project.ui.theme.Secondary
import com.example.web_api_project.ui.theme.Accent
import com.example.web_api_project.ui.theme.Tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    newborn: NewbornEntity,
    repository: NewbornRepository
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(newborn.isFavorite) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalji", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nazad", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            repository.toggleFavoriteByFields(
                                entity = newborn.entity,
                                municipality = newborn.municipality,
                                year = newborn.year,
                                month = newborn.month,
                                isFav = isFavorite
                            )
                            isFavorite = !isFavorite
                        }
                    }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Ukloni iz favorita" else "Dodaj u favorite",
                            tint = if (isFavorite) Accent else Color.White
                        )
                    }
                    IconButton(onClick = {
                        val shareText = "Entitet: ${newborn.entity}\nKanton: ${newborn.canton}\nOpština: ${newborn.municipality}\nUstanova: ${newborn.institution}\nGodina: ${newborn.year}\nMjesec: ${newborn.month}\nUkupno: ${newborn.total} (M: ${newborn.maleTotal}, Ž: ${newborn.femaleTotal})\nAžurirano: ${newborn.dateUpdate}"
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Dijeli", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary
                )
            )
        },
        containerColor = Tertiary
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Tertiary)
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Entitet: ${newborn.entity}", style = MaterialTheme.typography.titleMedium, color = Primary)
                    newborn.canton?.let { Text("Kanton: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                    newborn.municipality?.let { Text("Opština: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                    newborn.institution?.let { Text("Ustanova: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                    Text("Godina: ${newborn.year}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                    Text("Mjesec: ${newborn.month}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                    Text("Ukupno: ${newborn.total} (M: ${newborn.maleTotal}, Ž: ${newborn.femaleTotal})", style = MaterialTheme.typography.bodyLarge, color = Primary)
                    Text("Ažurirano: ${newborn.dateUpdate}", style = MaterialTheme.typography.bodySmall, color = Tertiary)
                }
            }
        }
    }
} 