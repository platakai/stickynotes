package com.example.stickynotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stickynotes.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onAuthSuccess: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val error by viewModel.authError.collectAsState()

    // Kada se user promijeni u nenull, pozovi callback
    LaunchedEffect(user) {
        if (user != null) {
            onAuthSuccess()
        }
    }

    // Lokalne varijable za unos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (user != null) {
            // Ovdje se ne bi trebalo doći zbog LashedEffect-a
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ulogirani ste kao ${user!!.email}")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Prijava / Registracija", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Lozinka") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.signIn(email, password) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Prijava")
                    }
                    Button(
                        onClick = { viewModel.signUp(email, password) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Registracija")
                    }
                }
            }
        }
    }
}