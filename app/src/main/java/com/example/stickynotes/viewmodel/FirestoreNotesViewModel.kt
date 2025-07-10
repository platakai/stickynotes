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
 * - "id": automatski Firestore dokument ID
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

    /** Počne slušati promjene u kolekciji za zadanog korisnika */
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

    /** Dodaje novu bilješku u Firestore */
    fun addNote(text: String, uid: String) = viewModelScope.launch {
        val data = mapOf(
            "text" to text,
            "uid" to uid,
            "timestamp" to System.currentTimeMillis()
        )
        collection.add(data)
    }

    /** Ažurira postojeću bilješku po ID-u */
    fun updateNote(id: String, newText: String) = viewModelScope.launch {
        collection.document(id)
            .update("text", newText)
    }

    /** Briše bilješku po ID-u */
    fun deleteNote(id: String) = viewModelScope.launch {
        collection.document(id).delete()
    }
}
