package com.conectarsalud.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.conectarsalud.app.data.model.Turno
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val turnoRepository: TurnoRepository
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _proximoTurno = MutableLiveData<Turno?>()
    val proximoTurno: LiveData<Turno?> = _proximoTurno

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        val currentUser = userRepository.currentUser ?: return
        _isLoading.value = true

        viewModelScope.launch {
            // Asegurar que el perfil exista en Firestore (fix para usuarios previos)
            userRepository.ensureUserProfileExists(currentUser)

            // Cargar nombre: Firestore → displayName → prefijo del email → "Usuario"
            val user = userRepository.getUser(currentUser.uid)
            _userName.value = user?.nombre
                ?: currentUser.displayName?.split(" ")?.firstOrNull()
                ?: currentUser.email
                    ?.substringBefore("@")
                    ?.replaceFirstChar { it.uppercase() }
                ?: "Usuario"

            // Load next upcoming turno
            _proximoTurno.value = turnoRepository.getProximoTurno(currentUser.uid)
            _isLoading.value = false
        }
    }

    fun signOut() {
        userRepository.signOut()
    }

    fun refreshData() {
        loadData()
    }
}

class HomeViewModelFactory(
    private val userRepository: UserRepository,
    private val turnoRepository: TurnoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(userRepository, turnoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
