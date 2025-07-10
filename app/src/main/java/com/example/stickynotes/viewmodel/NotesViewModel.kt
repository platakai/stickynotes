package com.example.stickynotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    // Drži listu bilješki
    private val _notes = MutableStateFlow<List<String>>(emptyList())
    val notes: StateFlow<List<String>> = _notes

    // Dodaj novu bilješku
    fun add(note: String) {
        viewModelScope.launch {
            _notes.value = _notes.value + note
        }
    }

    // Ukloni bilješku na indexu
    fun removeAt(index: Int) {
        viewModelScope.launch {
            _notes.value = _notes.value.toMutableList().also { it.removeAt(index) }
        }
    }

    // Očisti sve bilješke
    fun clearAll() {
        viewModelScope.launch {
            _notes.value = emptyList()
        }
    }
}
