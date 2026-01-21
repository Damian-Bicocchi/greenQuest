package com.example.greenquest.viewmodel

import com.example.greenquest.Articulo

class TiendaViewModel {

    fun comprarArticulo(articuloId: Int): Boolean{
        // Lógica para comprar el artículo
        return true
    }

    fun obtenerArticulosDisponibles(): List<Articulo>{

        return listOf(
            Articulo(1, "Planta de interior", "500 puntos", null, false),
            Articulo(2, "Kit de reciclaje", "300 puntos", null, false),
            Articulo(3, "Bicicleta ecológica", "1500 puntos", null, false),
            Articulo(4, "Botella reutilizable", "200 puntos", null, false),
            Articulo(5, "Panel solar portátil", "2500 puntos", null, false),
            Articulo(3, "Bicicleta ecológica", "1500 puntos", null, false),
            Articulo(4, "Botella reutilizable", "200 puntos", null, false),
            Articulo(5, "Panel solar portátil", "2500 puntos", null, false)

        )
    }
}