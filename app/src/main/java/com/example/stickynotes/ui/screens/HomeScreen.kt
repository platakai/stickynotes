package com.example.stickynotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stickynotes.viewmodel.NotesViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    notesVM: NotesViewModel = viewModel()
) {
    // Local state for input
    var newNote by rememberSaveable { mutableStateOf("") }
    val notes by notesVM.notes.collectAsState()

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Input and add button
                OutlinedTextField(
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
                // Notes list: scrollable
                if (notes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nema bilješki. Dodaj prvu!")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(notes) { index, note ->
                            Text(
                                text = "• $note",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { notesVM.removeAt(index) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Sign out button
                Button(
                    onClick = {
                        notesVM.clearAll()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Odjava")
                }
            }
        }
    )
}
