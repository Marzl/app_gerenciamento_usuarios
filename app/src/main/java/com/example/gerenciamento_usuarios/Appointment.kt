package com.example.gerenciamento_usuarios
import java.util.UUID

data class Appointment (
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val exame: String,
    val date: String,
    val time: String
)