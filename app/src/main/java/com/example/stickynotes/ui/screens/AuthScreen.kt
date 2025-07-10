package com.example.stickynotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stickynotes.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onAuthSuccess: () -> Unit
) {
    val user by viewModel.user.collectAsState()
    val error by viewModel.authError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Navigiraj kad se pojavi user
    LaunchedEffect(user) {
        if (user != null) onAuthSuccess()
    }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (user != null) {
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

                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.signIn(email, password) },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "..." else "Prijava")
                    }
                    Button(
                        onClick = { viewModel.signUp(email, password) },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "..." else "Registracija")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { viewModel.resetPassword(email) },
                    enabled = !isLoading
                ) {
                    Text("Zaboravili ste lozinku?")
                }
            }
        }
    }
}