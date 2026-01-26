package com.example.greenquest.viewmodel

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.Articulo
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository

class TiendaViewModel {

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

    fun obtenerArticulosDisponibles(): List<Articulo>{
        return articulosTienda
    }

    fun actualizarArticulosAdquiridos(usuario: User) {
        val articulos = obtenerArticulosDisponibles()
        for(articulo in articulos) {
            if(usuario.articulos_adquiridos.contains(articulo.id)) {
                articulo.adquirido = true
            }
        }
    }

    fun articulosAdquiridosIds(usuario : User): List<Articulo> {
        val listaArticulos = mutableListOf<Articulo>()
        val articulos = obtenerArticulosDisponibles()
        for(articuloId in usuario.articulos_adquiridos) {
            listaArticulos.add(articulos.first { it.id == articuloId })

        }
        return listaArticulos
    }
    suspend fun comprarArticulo(articulo: Articulo): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        Log.d("TIENDA", "Intentando comprar articulo ${articulo.id} por ${articulo.valor} monedas. Usuario tiene ${usuario.articulos_adquiridos} articulosIds")
        if(usuario.monedas >= articulo.valor) {
            usuario.monedas -= articulo.valor
            usuario.articulos_adquiridos.add(articulo.id)
            articulo.adquirido = true
            UsuarioRepository.actualizarUsuarioLocal(usuario)
            return true
        }else{
            return false
        }
    }
}