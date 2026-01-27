package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.Articulo
import com.example.greenquest.Provider.ArticulosProvider
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository

class TiendaViewModel : ViewModel() {

    private val articulosTienda = ArticulosProvider.articulosTienda

    fun obtenerArticulosDisponibles(): List<Articulo>{
        return articulosTienda
    }

    suspend fun actualizarArticulosAdquiridos() {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
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
    suspend fun actualizarMonedasUsuario(): String {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        return usuario.monedas.toString()
    }
}