package com.example.stickynotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel za bilješke pohranjene u Firestore kolekciji "notes".
 * Svaki dokument ima polja:
 * - "text": String s tekstom bilješke
 * - "uid": String s UID-om korisnika
 * - "timestamp": Long s vremenom kreiranja
 */
class FirestoreNotesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("notes")

    private val _notes = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    /** Emits listu parova (docId, noteText) */
    val notes: StateFlow<List<Pair<String, String>>> = _notes

    /** Počinje slušati promjene u kolekciji za zadanog korisnika */
    fun startListening(uid: String) {
        collection
            .whereEqualTo("uid", uid)
            .orderBy("timestamp")
            .addSnapshotListener { snapshot: QuerySnapshot?, error ->
                if (error != null) {
                    // grešku eventualno obradiš ovdje
                    return@addSnapshotListener
                }
                updateList(snapshot)
            }
    }

    private fun updateList(snapshot: QuerySnapshot?) {
        val list = snapshot
            ?.map { doc ->
                val id = doc.id
                val text = doc.getString("text") ?: ""
                id to text
            } ?: emptyList()
        _notes.value = list
    }

    /** Dodaje novu bilješku za trenutno prijavljenog korisnika */
    fun add(newNote: String) {
        val uid = Firebase.auth.currentUser?.uid ?: return
        addNote(newNote, uid)
    }

    /** Uklanja bilješku na zadanom indexu iz liste */
    fun removeAt(index: Int) {
        val list = _notes.value
        if (index < 0 || index >= list.size) return
        val id = list[index].first
        deleteNote(id)
    }

    /** Briše sve bilješke korisnika iz Firestore i lokalne liste */
    fun clearAll() {
        viewModelScope.launch {
            // iz trenutne kolekcije ukloni sve dokumente
            _notes.value.forEach { (id, _) ->
                collection.document(id).delete()
            }
            // lokalno očisti listu
            _notes.value = emptyList()
        }
    }

    /** Interna: Firestore dodavanje */
    fun addNote(text: String, uid: String) = viewModelScope.launch {
        val data = mapOf(
            "text" to text,
            "uid" to uid,
            "timestamp" to System.currentTimeMillis()
        )
        collection.add(data)
    }

    /** Ažurira postojeću bilješku po ID-u */
    fun updateNote(id: String, newText: String) {
        viewModelScope.launch {
            collection.document(id)
                .update("text", newText)
        }
    }

    /** Briše bilješku po ID-u */
    fun deleteNote(id: String) = viewModelScope.launch {
        collection.document(id).delete()
    }
}
