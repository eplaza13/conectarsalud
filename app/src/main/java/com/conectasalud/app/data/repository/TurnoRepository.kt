package com.conectasalud.app.data.repository

import com.conectasalud.app.data.model.Turno
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            .orderBy("creadoEn", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val turnos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Turno::class.java)?.copy(id = doc.id)
                } ?: emptyList()
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
            val snapshot = firestore.collection("turnos")
                .whereEqualTo("userId", userId)
                .whereEqualTo("estado", "pendiente")
                .orderBy("creadoEn", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(Turno::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            null
        }
    }
}
