package com.example.stickynotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    // Flow koje emitiraju trenutno ulogiranog korisnika i eventualnu grešku
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = auth.currentUser
                        _authError.value = null
                    } else {
                        _authError.value = task.exception?.localizedMessage
                    }
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _user.value = auth.currentUser
                        _authError.value = null
                    } else {
                        _authError.value = task.exception?.localizedMessage
                    }
                }
        }
    }

    fun signOut() {
        auth.signOut()
        _user.value = null
        _authError.value = null
    }
}