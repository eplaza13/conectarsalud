package com.conectarsalud.app.ui.turnos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.conectarsalud.app.data.model.Turno
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MisTurnosViewModel(
    private val turnoRepository: TurnoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _turnos = MutableLiveData<List<Turno>>(emptyList())
    val turnos: LiveData<List<Turno>> = _turnos

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadTurnos()
    }

    private fun loadTurnos() {
        val userId = userRepository.currentUser?.uid ?: return
        _isLoading.value = true

        viewModelScope.launch {
            turnoRepository.getMisTurnos(userId)
                .catch { e ->
                    _error.value = e.message
                    _isLoading.value = false
                }
                .collect { turnos ->
                    _turnos.value = turnos
                    _isLoading.value = false
                }
        }
    }

    fun cancelarTurno(turnoId: String) {
        viewModelScope.launch {
            turnoRepository.cancelarTurno(turnoId)
        }
    }
}

class MisTurnosViewModelFactory(
    private val turnoRepository: TurnoRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MisTurnosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MisTurnosViewModel(turnoRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
