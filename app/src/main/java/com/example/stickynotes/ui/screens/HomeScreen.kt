package com.example.stickynotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.stickynotes.viewmodel.AuthViewModel
import com.example.stickynotes.viewmodel.FirestoreNotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    screenVM: FirestoreNotesViewModel = viewModel(),
    authVM: AuthViewModel = viewModel(),
    notesVM: FirestoreNotesViewModel
) {
    // Observe user and notes
    val currentUser by authVM.user.collectAsState()
    val notes by screenVM.notes.collectAsState()

    // Start listening once user is available
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { screenVM.startListening(it) }
    }

    // Local UI state
    var newNote by rememberSaveable { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Pair<String, String>?>(null) }
    var editText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moje bilješke") },
                actions = {
                    TextButton(onClick = {
                        authVM.signOut()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Text("Odjava")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Input row for new note
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newNote,
                    onValueChange = { newNote = it },
                    label = { Text("Naslov bilješke") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        currentUser?.uid?.let { uid ->
                            screenVM.add(newNote)
                        }
                        newNote = ""
                    },
                    enabled = newNote.isNotBlank()
                ) {
                    Text("Dodaj")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of notes
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nema bilješki.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notes) { (id, title) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                editingNote = id to title
                                editText = title
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Uredi")
                            }
                            IconButton(onClick = {
                                screenVM.deleteNote(id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Obriši")
                            }
                        }
                    }
                }
            }
        }

        // Edit dialog
        editingNote?.let { (id, _) ->
            AlertDialog(
                onDismissRequest = { editingNote = null },
                title = { Text("Uredi bilješku") },
                text = {
                    OutlinedTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        label = { Text("Novi naslov") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        screenVM.updateNote(id, editText)
                        editingNote = null
                    }) {
                        Text("Spremi")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingNote = null }) {
                        Text("Odustani")
                    }
                }
            )
        }
    }
}
