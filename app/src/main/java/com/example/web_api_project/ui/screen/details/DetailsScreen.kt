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
import com.example.web_api_project.ui.screen.home.NewbornViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import com.example.web_api_project.data.local.entity.toDomain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    newbornId: Int,
    userSessionViewModel: UserSessionViewModel,
    viewModel: NewbornViewModel = viewModel()
) {
    val email by userSessionViewModel.userEmail.collectAsState()
    val emailValue = email
    val context = LocalContext.current
    var newborn by remember { mutableStateOf<com.example.web_api_project.data.local.entity.NewbornEntity?>(null) }
    LaunchedEffect(newbornId) {
        val nb = viewModel.db.newbornDao().getById(newbornId)
        println("DEBUG DetailsScreen: newbornId=$newbornId, newborn=$nb")
        newborn = nb
    }
    LaunchedEffect(emailValue) {
        if (emailValue == null) {
            userSessionViewModel.login("test@email.com")
        } else {
            viewModel.refreshUserAndFavorites(emailValue)
        }
    }
    newborn?.let { nb ->
        val favoriteIds by viewModel.favoriteIds.collectAsState()
        val isFav = favoriteIds.contains(nb.id)
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
                        IconButton(onClick = { viewModel.toggleFavorite(nb.toDomain()) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = if (isFav) "Ukloni iz favorita" else "Dodaj u favorite",
                                tint = if (isFav) Color.Yellow else Color.White
                            )
                        }
                        IconButton(onClick = {
                            val shareText = "Entitet: ${nb.entity}\nKanton: ${nb.canton}\nOpština: ${nb.municipality}\nUstanova: ${nb.institution}\nGodina: ${nb.year}\nMjesec: ${nb.month}\nUkupno: ${nb.total} (M: ${nb.maleTotal}, Ž: ${nb.femaleTotal})\nAžurirano: ${nb.dateUpdate}"
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
                        Text("Entitet: ${nb.entity}", style = MaterialTheme.typography.titleMedium, color = Primary)
                        nb.canton?.let { Text("Kanton: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        nb.municipality?.let { Text("Opština: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        nb.institution?.let { Text("Ustanova: $it", style = MaterialTheme.typography.bodyLarge, color = Secondary) }
                        Text("Godina: ${nb.year}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Mjesec: ${nb.month}", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Ukupno: ${nb.total} (M: ${nb.maleTotal}, Ž: ${nb.femaleTotal})", style = MaterialTheme.typography.bodyLarge, color = Primary)
                        Text("Ažurirano: ${nb.dateUpdate}", style = MaterialTheme.typography.bodySmall, color = Tertiary)
                    }
                }
            }
        }
    }
} 