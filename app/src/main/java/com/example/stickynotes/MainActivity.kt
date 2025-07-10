package com.example.stickynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stickynotes.ui.theme.StickyNotesTheme
import com.example.stickynotes.ui.components.BottomNavigationBar
import com.example.stickynotes.ui.screens.LoginScreen
import com.example.stickynotes.ui.screens.HomeScreen
import com.example.stickynotes.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Pamti se dark mode kroz rotacije
            var isDarkMode by rememberSaveable { mutableStateOf(false) }
            // Nav controller za navigaciju
            val navController = rememberNavController()

            StickyNotesTheme(darkTheme = isDarkMode) {
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(navController)
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
