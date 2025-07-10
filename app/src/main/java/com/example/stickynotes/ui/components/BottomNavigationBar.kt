package com.example.stickynotes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.List, contentDescription = "Bilješke") },
            label = { Text("Bilješke") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Postavke") },
            label = { Text("Postavke") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("login") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Odjava") },
            label = { Text("Odjava") }
        )
    }
}
