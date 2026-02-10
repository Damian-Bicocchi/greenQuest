package com.example.greenquest.Provider

import com.example.greenquest.R
import com.example.greenquest.database.tienda.Articulo

class ArticulosProvider {
    companion object {
        var articulosTienda = listOf(
            Articulo(1, "Reciclador Basico", 50,R.drawable.recicladorbasico, false),
            Articulo(2, "Reciclador Avanzado", 150,R.drawable.recicladorlegend, false),
            Articulo(3, "Reciclador Experto", 300, R.drawable.recicladorchampion, false),
            Articulo(4, "Eco Guerrero",500, R.drawable.ecolegend, false),
            Articulo(5, "Eco Guardiana",800,R.drawable.ecoguardian, false),
            Articulo(6, "Eco Maestro",1200,R.drawable.ecogodness, false),
            Articulo(7, "Eco Supremo",2000,R.drawable.superreciclador, false),
            Articulo(8, "Eco Reina", 2500, R.drawable.ecoqueen, false),
            Articulo(9, "Eco Legendario", 5000,R.drawable.ecomaster, false)
        )
        fun actualizarArticulosAdquiridos(articulosAdquiridos: List<Int>) {
            for (articulo in articulosTienda) {
                if (articulosAdquiridos.contains(articulo.id)) {
                    articulo.adquirido = true
                }
            }
        }
        fun cerrarSesionArticulos() {
            for (articulo in articulosTienda) {
                articulo.adquirido = false
            }
        }
    }

}