package com.example.gerenciamento_usuarios
import java.util.UUID
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val username: String,
    val password: String
)
