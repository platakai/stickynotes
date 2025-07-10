package com.example.stickynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.stickynotes.ui.components.BottomNavigationBar
import com.example.stickynotes.ui.screens.AuthScreen
import com.example.stickynotes.ui.screens.HomeScreen
import com.example.stickynotes.ui.screens.SettingsScreen
import com.example.stickynotes.ui.theme.StickyNotesTheme
import com.example.stickynotes.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkMode by rememberSaveable { mutableStateOf(false) }
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.user.collectAsState()

            val navController = rememberNavController()
            val start = if (currentUser != null) "home" else "login"

            StickyNotesTheme(darkTheme = isDarkMode) {
                Scaffold(
                    bottomBar = {
                        if (currentUser != null) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = start,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            AuthScreen(
                                viewModel = authViewModel,
                                onAuthSuccess = { navController.navigate("home") }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController, isDarkMode) { isDarkMode = it }
                        }
                    }
                }
            }
        }
    }
}
