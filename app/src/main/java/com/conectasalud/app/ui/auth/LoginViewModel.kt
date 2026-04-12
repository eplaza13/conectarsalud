package com.conectasalud.app.ui.auth

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.conectasalud.app.data.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun isUserLoggedIn(): Boolean = userRepository.currentUser != null

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Por favor completa todos los campos")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signInWithEmail(email.trim(), password)
            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                AuthState.Error(result.exceptionOrNull()?.localizedMessage ?: "Error al iniciar sesión")
            }
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val result = userRepository.signInWithGoogle(account.idToken!!)
                _authState.value = if (result.isSuccess) {
                    AuthState.Success
                } else {
                    AuthState.Error(result.exceptionOrNull()?.localizedMessage ?: "Error con Google")
                }
            } catch (e: ApiException) {
                _authState.value = AuthState.Error("No se pudo iniciar con Google (${e.statusCode})")
            }
        }
    }
}

class LoginViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
