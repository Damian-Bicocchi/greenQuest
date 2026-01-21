package com.example.greenquest.apiParameters

// Serializer para el modelo Estacion. Define los campos que se mostrarán en la API.
data class Estacion(
    val id: Int,
    val nombre: String,
    val latitud: Double,
    val longitud: Double
)