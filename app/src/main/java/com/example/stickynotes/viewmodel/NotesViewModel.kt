package com.example.stickynotes.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<String>>(emptyList())
    val notes: StateFlow<List<String>> = _notes

    fun add(note: String) {
        _notes.value = _notes.value + note
    }
    fun removeAt(index: Int) {
        _notes.value = _notes.value.filterIndexed { i, _ -> i != index }
    }
}
