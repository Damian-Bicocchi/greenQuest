package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.greenquest.provider.ArticulosProvider
import com.example.greenquest.database.tienda.Articulo
import com.example.greenquest.repository.TiendaAdquiridosRepository
import com.example.greenquest.repository.UsuarioRepository

class TiendaViewModel : ViewModel() {

    private val articulosTienda = ArticulosProvider.articulosTienda

    fun obtenerArticulosDisponibles(): List<Articulo>{
        return articulosTienda
    }

    suspend fun actualizarArticulosAdquiridos() {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        val tiendaUsuario = TiendaAdquiridosRepository.obtenerTiendaPorUsuario(usuario.uid)
        ArticulosProvider.actualizarArticulosAdquiridos(tiendaUsuario.articulosAdquiridos)
    }

    suspend fun comprarArticulo(articulo: Articulo): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        Log.d("TIENDA", "Intentando comprar articulo ${articulo.id} por ${articulo.valor} monedas. Usuario tiene ${usuario.articulosAdquiridos} articulosIds")
        if(TiendaAdquiridosRepository.comprarArticulo(articulo.valor,articulo.id,usuario.uid)) {
            articulo.adquirido = true
            return true
        }else{
            return false
        }
    }
    suspend fun actualizarMonedasUsuario(): String {
        val idUsuario = UsuarioRepository.obtenerUsuarioLocal()!!.uid
        return TiendaAdquiridosRepository.obtenerMonedasUsuario(idUsuario).toString()
    }
}