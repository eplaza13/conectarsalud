package com.conectarsalud.app.data.repository

import android.util.Log
import com.conectarsalud.app.data.model.Turno
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TurnoRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun reservarTurno(turno: Turno): Result<String> {
        return try {
            val turnoWithTimestamp = turno.copy(creadoEn = Timestamp.now())
            val doc = firestore.collection("turnos").add(turnoWithTimestamp).await()
            Result.success(doc.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMisTurnos(userId: String): Flow<List<Turno>> = callbackFlow {
        val listener = firestore.collection("turnos")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error en listener de turnos: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val turnos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Turno::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.creadoEn?.seconds ?: 0L } ?: emptyList()
                trySend(turnos)
            }
        awaitClose { listener.remove() }
    }

    suspend fun cancelarTurno(turnoId: String): Result<Unit> {
        return try {
            firestore.collection("turnos")
                .document(turnoId)
                .update("estado", "cancelado")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProximoTurno(userId: String): Turno? {
        return try {
            // Query simple con un solo whereEqualTo — no requiere índice compuesto
            // El ordenamiento y filtro de estado los hacemos en memoria
            val snapshot = firestore.collection("turnos")
                .whereEqualTo("userId", userId)
                .get()
                .await()
            Log.d(TAG, "Turnos encontrados para userId=$userId: ${snapshot.size()}")
            snapshot.documents
                .mapNotNull { doc -> doc.toObject(Turno::class.java)?.copy(id = doc.id) }
                .filter { it.estado == "pendiente" }
                .sortedByDescending { it.creadoEn?.seconds ?: 0L }
                .firstOrNull()
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo próximo turno: ${e.message}", e)
            null
        }
    }

    companion object {
        private const val TAG = "TurnoRepository"
    }
}
