package com.example.stickynotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.stickynotes.viewmodel.NotesViewModel
import com.example.stickynotes.ui.components.BottomNavigationBar

@Composable
fun HomeScreen(
    navController: NavHostController,
    notesVM: NotesViewModel = viewModel()
) {
    var newNote by rememberSaveable { mutableStateOf("") }
    val notes by notesVM.notes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        TextField(
            value = newNote,
            onValueChange = { newNote = it },
            label = { Text("Unesi bilješku") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (newNote.isNotBlank()) {
                    notesVM.add(newNote)
                    newNote = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj")
        }
        Spacer(modifier = Modifier.height(16.dp))
        notes.forEachIndexed { index, note ->
            Text(
                text = "• $note",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { notesVM.removeAt(index) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Odjava")
        }
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController)
    }
}
