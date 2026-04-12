package com.conectasalud.app.data.model

data class User(
    val uid: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = "",
    val email: String = "",
    val photoUrl: String? = null
)
