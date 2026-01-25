package com.example.greenquest.viewmodel

import com.example.greenquest.Articulo
import com.example.greenquest.R
import com.example.greenquest.repository.UsuarioRepository

class TiendaViewModel {


    fun obtenerArticulosDisponibles(): List<Articulo>{
        return listOf(
            Articulo(1, "Planta de interior", "500 puntos", R.drawable.outline_person_24, false),
            Articulo(2, "Kit de reciclaje", "300 puntos", R.drawable.plastico50, false),
            Articulo(3, "Bicicleta ecológica", "1500 puntos", R.drawable.metal100, false),
            Articulo(4, "Botella reutilizable", "200 puntos", R.drawable.metal10, false),
            Articulo(5, "Panel solar portátil", "2500 puntos", R.drawable.metal10, false),
            Articulo(6, "Lámpara LED", "800 puntos", R.drawable.carton50, false),
            Articulo(7, "Cargador solar", "1200 puntos", R.drawable.plastico10, false),
            Articulo(8, "Mochila ecológica", "600 puntos", R.drawable.podium, false)
        )
    }

    suspend fun comprarArticulo(articulo: Articulo): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        if(usuario.monedas >= articulo.valor.toInt()) {
            usuario.monedas -= articulo.valor.toInt()
            usuario.articulos_adquiridos.add(articulo.id)
            articulo.adquirido = true
            UsuarioRepository.actualizarUsuarioLocal(usuario)
            return true
        }else{
            return false
        }
    }
}