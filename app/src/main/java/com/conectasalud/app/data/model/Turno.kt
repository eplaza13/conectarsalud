package com.conectasalud.app.data.model

import com.google.firebase.Timestamp

data class Turno(
    val id: String = "",
    val userId: String = "",
    val especialidad: String = "",
    val clinica: String = "",
    val fecha: String = "",
    val hora: String = "",
    val estado: String = "pendiente", // pendiente | completado | cancelado
    val creadoEn: Timestamp? = null
)
