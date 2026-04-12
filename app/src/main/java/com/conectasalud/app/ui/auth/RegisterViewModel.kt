package com.conectasalud.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.conectasalud.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun register(
        nombre: String,
        apellido: String,
        dni: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // Validations
        when {
            nombre.isBlank() || apellido.isBlank() || dni.isBlank() || email.isBlank() || password.isBlank() -> {
                _authState.value = AuthState.Error("Por favor completa todos los campos")
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _authState.value = AuthState.Error("Correo electrónico inválido")
                return
            }
            password.length < 6 -> {
                _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
                return
            }
            password != confirmPassword -> {
                _authState.value = AuthState.Error("Las contraseñas no coinciden")
                return
            }
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.signUpWithEmail(
                email = email.trim(),
                password = password,
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                dni = dni.trim()
            )
            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                AuthState.Error(result.exceptionOrNull()?.localizedMessage ?: "Error al crear la cuenta")
            }
        }
    }
}

class RegisterViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
