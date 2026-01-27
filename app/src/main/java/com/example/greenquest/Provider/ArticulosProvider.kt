package com.example.greenquest.Provider

import com.example.greenquest.Articulo
import com.example.greenquest.R

class ArticulosProvider {
    companion object {
        val articulosTienda = listOf(
            Articulo(1, "Planta de interior", 500, R.drawable.outline_person_24, false),
            Articulo(2, "Kit de reciclaje", 300, R.drawable.plastico50, false),
            Articulo(3, "Bicicleta ecológica", 1500, R.drawable.metal100, false),
            Articulo(4, "Botella reutilizable", 200, R.drawable.metal10, false),
            Articulo(5, "Panel solar portátil", 2500 , R.drawable.metal10, false),
            Articulo(6, "Lámpara LED", 800, R.drawable.carton50, false),
            Articulo(7, "Cargador solar", 1200, R.drawable.plastico10, false),
            Articulo(8, "Mochila ecológica", 600, R.drawable.podium, false)
        )




    }
}