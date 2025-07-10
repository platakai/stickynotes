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
import com.example.stickynotes.viewmodel.FirestoreNotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Dark mode flag
            var isDarkMode by rememberSaveable { mutableStateOf(false) }

            // Auth ViewModel and current user state
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.user.collectAsState()

            // Firestore ViewModel for notes
            val firestoreVM: FirestoreNotesViewModel = viewModel()

            // Start listening to Firestore when user logs in
            LaunchedEffect(currentUser) {
                currentUser?.uid?.let { uid ->
                    firestoreVM.startListening(uid)
                }
            }

            // Navigation controller
            val navController = rememberNavController()
            val startDestination = if (currentUser != null) "home" else "login"

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
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            AuthScreen(
                                viewModel = authViewModel,
                                onAuthSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                navController = navController,
                                notesVM = firestoreVM,
                                authVM = authViewModel
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                navController = navController,
                                isDarkMode = isDarkMode,
                                onToggleDarkMode = { isDarkMode = it }
                            )
                        }
                    }
                }
            }
        }
    }
}
