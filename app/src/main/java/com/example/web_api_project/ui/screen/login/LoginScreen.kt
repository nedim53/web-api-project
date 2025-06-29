package com.example.web_api_project.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.web_api_project.ui.UserSessionViewModel
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.web_api_project.utils.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, userSessionViewModel: UserSessionViewModel, viewModel: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val uiEvent by viewModel.uiEvent.collectAsState()
    var shouldReset by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiEvent) {
        try {
            when (uiEvent) {
                is LoginUiEvent.Success -> {
                    userSessionViewModel.login((uiEvent as LoginUiEvent.Success).email)
                    // Sačuvaj email u DataStore
                    scope.launch {
                        context.dataStore.edit { prefs ->
                            prefs[stringPreferencesKey("last_email")] = (uiEvent as LoginUiEvent.Success).email
                        }
                    }
                    navController.navigate("onboarding") {
                        popUpTo("login") { inclusive = true }
                    }
                    shouldReset = true
                }
                is LoginUiEvent.Error -> {
                    error = (uiEvent as LoginUiEvent.Error).message
                    shouldReset = true
                }
                LoginUiEvent.Idle -> {}
            }
        } catch (e: Exception) {
            println("LoginScreen LaunchedEffect error: ${e.message}")
        }
    }
    LaunchedEffect(shouldReset) {
        if (shouldReset) {
            viewModel.resetEvent()
            shouldReset = false
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Prijava", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Lozinka") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        error = "Unesite email i lozinku."
                    } else {
                        error = null
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Prijavi se")
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Nemate nalog? Registrujte se")
            }
        }
    }
} 