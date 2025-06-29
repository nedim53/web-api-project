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
import com.example.web_api_project.data.repository.DeathsRepository
import com.example.web_api_project.data.local.entity.DeathsEntity
import com.example.web_api_project.ui.theme.Primary
import com.example.web_api_project.ui.theme.Secondary
import com.example.web_api_project.ui.theme.Accent
import com.example.web_api_project.ui.theme.Tertiary
import com.example.web_api_project.ui.screen.home.DeathsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import com.example.web_api_project.data.local.entity.toDomain
import androidx.compose.foundation.lazy.LazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeathsDetailsScreen(
    navController: NavController,
    deathsId: Int,
    userSessionViewModel: UserSessionViewModel,
    viewModel: DeathsViewModel = viewModel()
) {
    val email by userSessionViewModel.userEmail.collectAsState()
    val emailValue = email
    val context = LocalContext.current
    var deaths by remember { mutableStateOf<com.example.web_api_project.data.local.entity.DeathsEntity?>(null) }
    LaunchedEffect(deathsId) {
        val d = viewModel.db.deathsDao().getById(deathsId)
        println("DEBUG DeathsDetailsScreen: deathsId=$deathsId, deaths=$d")
        deaths = d
    }
    LaunchedEffect(emailValue) {
        if (emailValue == null) {
            userSessionViewModel.login("test@email.com")
        } else {
            viewModel.refreshUserAndFavorites(emailValue)
        }
    }
    deaths?.let { d ->
        val favoriteIds by viewModel.favoriteIds.collectAsState()
        val isFav = favoriteIds.contains(d.id)
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalji smrti", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nazad", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite(d.toDomain()) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = if (isFav) "Ukloni iz favorita" else "Dodaj u favorite",
                                tint = if (isFav) Color.Yellow else Color.White
                            )
                        }
                        IconButton(onClick = {
                            val shareText = "Entitet: ${d.entity}\nKanton: ${d.canton}\nOpština: ${d.municipality}\nUstanova: ${d.institution}\nGodina: ${d.year}\nMjesec: ${d.month}\nUkupno: ${d.total} (M: ${d.maleTotal}, Ž: ${d.femaleTotal})\nAžurirano: ${d.dateUpdate}"
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
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Entitet: ${d.entity}", style = MaterialTheme.typography.titleMedium, color = Primary)
                        d.canton?.let { Text("Kanton: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        d.municipality?.let { Text("Opština: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        d.institution?.let { Text("Ustanova: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        Text("Godina: ${d.year}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Mjesec: ${d.month}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Ukupno: ${d.total} (M: ${d.maleTotal}, Ž: ${d.femaleTotal})", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Ažurirano: ${d.dateUpdate}", style = MaterialTheme.typography.bodySmall, color = Tertiary)
                        Spacer(Modifier.height(24.dp))
                        Text("Grafikon broja smrti", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        BarChart(
                            values = listOf(
                                d.maleTotal ?: 0,
                                d.femaleTotal ?: 0,
                                d.total ?: 0
                            ),
                            labels = listOf("Muški", "Ženski", "Ukupno")
                        )
                    }
                }
            }
        }
    }
} 