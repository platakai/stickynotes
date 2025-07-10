package com.example.stickynotes.viewmodel

import android.util.Patterns
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

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length < 6) {
                _authError.value = "Neispravan email ili premala lozinka (min 6 znakova)."
                return@launch
            }
            _isLoading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
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
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isBlank()) {
                _authError.value = "Unesi valjani email i lozinku."
                return@launch
            }
            _isLoading.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        _user.value = auth.currentUser
                        _authError.value = null
                    } else {
                        _authError.value = task.exception?.localizedMessage
                    }
                }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _authError.value = "Unesi valjani email."
                return@launch
            }
            _isLoading.value = true
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    _isLoading.value = false
                    if (task.isSuccessful) {
                        _authError.value = "Link za reset lozinke poslan na $email"
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