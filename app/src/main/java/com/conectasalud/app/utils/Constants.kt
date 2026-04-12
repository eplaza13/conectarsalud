package com.conectasalud.app.utils

object Constants {

    const val COLLECTION_USERS = "users"
    const val COLLECTION_TURNOS = "turnos"

    val ESPECIALIDADES = listOf(
        "Pediatría",
        "Odontología",
        "Médico Clínico",
        "Ginecología",
        "Cardiología",
        "Dermatología",
        "Traumatología",
        "Neurología"
    )

    val CLINICAS = mapOf(
        "Pediatría" to "Centro Pediátrico del Sur",
        "Odontología" to "Clínica Dental Norte",
        "Médico Clínico" to "Centro Médico Central",
        "Ginecología" to "Centro de Salud Femenina",
        "Cardiología" to "Instituto Cardiovascular",
        "Dermatología" to "Clínica Dermatológica Plus",
        "Traumatología" to "Centro de Traumatología",
        "Neurología" to "Instituto Neurológico"
    )

    val HORARIOS_DISPONIBLES = listOf(
        "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00", "14:00",
        "14:30", "15:00", "15:30", "16:00"
    )
}
