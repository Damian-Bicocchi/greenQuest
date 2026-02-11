package com.example.greenquest.database.tienda

data class Articulo(
    val id: Int,
    val nombre: String,
    val valor: Int,
    val imagen: Int ?,
    var adquirido: Boolean
)
