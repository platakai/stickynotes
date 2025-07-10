package com.example.stickynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // 1) dark/light mode
            var isDarkMode by rememberSaveable { mutableStateOf(false) }

            // 2) instanciraj ViewModel i Collect user
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.user.collectAsState()

            // 3) nav controller i start screen
            val navController = rememberNavController()
            val startDestination = if (currentUser != null) "home" else "login"

            StickyNotesTheme(darkTheme = isDarkMode) {
                Scaffold(
                    // 4) sad je bottomBar COMPOSABLE – ima user-a u scope
                    bottomBar = {
                        if (currentUser != null) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
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
