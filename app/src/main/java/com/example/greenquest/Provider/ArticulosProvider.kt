package com.example.greenquest.Provider

import android.util.Log
import com.example.greenquest.database.tienda.Articulo
import com.example.greenquest.R

class ArticulosProvider {
    companion object {
        var articulosTienda = listOf(
            Articulo(1, "Reciclador Basico", 50,R.drawable.recicladorbasico),
            Articulo(2, "Reciclador Avanzado", 150,R.drawable.recicladorlegend),
            Articulo(3, "Reciclador Experto", 300,R.drawable.recicladorchampion),
            Articulo(4, "Eco Guerrero",500, R.drawable.ecolegend),
            Articulo(5, "Eco Guardiana",800,R.drawable.ecoguardian),
            Articulo(6, "Eco Maestro",1200,R.drawable.ecogodness),
            Articulo(7, "Eco Supremo",2000,R.drawable.superreciclador),
            Articulo(8, "Eco Reina", 2500, R.drawable.ecoqueen),
            Articulo(9, "Eco Legendario", 5000,R.drawable.ecomaster)
            )
        fun actualizarArticulosAdquiridos(articulosAdquiridos: List<Int>) {
            for (articulo in articulosTienda) {
                if (articulosAdquiridos.contains(articulo.id)) {
                    //articulo.adquirido = true
                    Log.d("a", "a")
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