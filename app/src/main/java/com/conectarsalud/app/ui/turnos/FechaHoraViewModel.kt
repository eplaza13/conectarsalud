package com.conectarsalud.app.ui.turnos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.conectarsalud.app.data.model.Turno
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val message: String) : BookingState()
}

class FechaHoraViewModel(
    private val turnoRepository: TurnoRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _bookingState = MutableLiveData<BookingState>(BookingState.Idle)
    val bookingState: LiveData<BookingState> = _bookingState

    fun reservarTurno(
        especialidad: String,
        clinica: String,
        fecha: String,
        hora: String
    ) {
        val userId = userRepository.currentUser?.uid
        if (userId == null) {
            _bookingState.value = BookingState.Error("Usuario no autenticado")
            return
        }

        if (fecha.isBlank()) {
            _bookingState.value = BookingState.Error("Por favor selecciona una fecha")
            return
        }

        if (hora.isBlank()) {
            _bookingState.value = BookingState.Error("Por favor selecciona un horario")
            return
        }

        viewModelScope.launch {
            _bookingState.value = BookingState.Loading
            val turno = Turno(
                userId = userId,
                especialidad = especialidad,
                clinica = clinica,
                fecha = fecha,
                hora = hora,
                estado = "pendiente"
            )
            val result = turnoRepository.reservarTurno(turno)
            _bookingState.value = if (result.isSuccess) {
                BookingState.Success
            } else {
                BookingState.Error(result.exceptionOrNull()?.localizedMessage ?: "Error al reservar")
            }
        }
    }
}

class FechaHoraViewModelFactory(
    private val turnoRepository: TurnoRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FechaHoraViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FechaHoraViewModel(turnoRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
