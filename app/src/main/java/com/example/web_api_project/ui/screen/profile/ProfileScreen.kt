package com.example.web_api_project.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, userSessionViewModel: UserSessionViewModel, viewModel: ProfileViewModel = viewModel()) {
    val email by userSessionViewModel.userEmail.collectAsState()
    val user by viewModel.user.collectAsState()

    LaunchedEffect(email) {
        email?.let { viewModel.loadUser(it) }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    if (user != null) {
                        Text(
                            "${user!!.firstName} ${user!!.lastName}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(user!!.email, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))
                        Divider()
                        Spacer(Modifier.height(16.dp))
                        user!!.phone?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Telefon:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        user!!.address?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Adresa:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    } else {
                        Text("Nema podataka o korisniku", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    userSessionViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Odjavi se")
            }
        }
    }
} 