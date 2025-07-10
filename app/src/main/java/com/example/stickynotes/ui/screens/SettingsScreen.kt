package com.example.stickynotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(
    navController: NavHostController,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    var fontSize by remember { mutableStateOf(18f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tamni način rada")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { onToggleDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Veličina teksta: ${fontSize.toInt()} sp")
        Slider(
            value = fontSize,
            onValueChange = { fontSize = it },
            valueRange = 12f..30f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Natrag")
        }
    }
}
