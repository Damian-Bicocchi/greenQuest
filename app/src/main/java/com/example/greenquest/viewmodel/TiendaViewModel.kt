package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.database.tienda.Articulo
import com.example.greenquest.Provider.ArticulosProvider
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterArticulo
import com.example.greenquest.database.tienda.ArticuloTiendaUI
import com.example.greenquest.database.tienda.CompraArticulo
import com.example.greenquest.database.tienda.TiendaConArticulos
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.TiendaAdquiridosRepository
import com.example.greenquest.repository.UsuarioRepository

class TiendaViewModel : ViewModel() {

    private val articulosTienda = ArticulosProvider.articulosTienda

    fun obtenerArticulosDisponibles(): List<Articulo>{
        return articulosTienda
    }

    suspend fun getListaParaAdapter(): MutableList<ArticuloTiendaUI> {
        val todosLosArticulos: List<Articulo> = obtenerArticulosDisponibles()

        val comprasRealizadasPorUsuario: List<CompraArticulo> =
            obtenerArticulosAdquiridos(
                UsuarioRepository.obtenerIdUsuarioActual()
            )

        val idsComprados = comprasRealizadasPorUsuario.map { it.articuloId }.toSet()

        val listaParaAdapter = mutableListOf<ArticuloTiendaUI>()
        for (elem in todosLosArticulos) {
            val comprado = idsComprados.contains(elem.id)
            listaParaAdapter.add(
                ArticuloTiendaUI(articulo = elem, adquirido = comprado)
            )
        }
        return listaParaAdapter
    }

    suspend fun actualizarArticulosAdquiridos() {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()!!
        val tiendaUsuario = TiendaAdquiridosRepository.obtenerTiendaPorUsuario(usuario.uid)
        //ArticulosProvider.actualizarArticulosAdquiridos(tiendaUsuario.articulosAdquiridos)
    }

    suspend fun comprarArticulo(articulo: Articulo): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal() ?: return false


        Log.d("TIENDA", "Intentando comprar articulo ${articulo.id} por ${articulo.valor} monedas. Usuario tiene ${usuario.articulos_adquiridos} articulosIds")
        return TiendaAdquiridosRepository.comprarArticulo(articulo.valor,articulo.id,usuario.uid)
    }
    suspend fun actualizarMonedasUsuario(): String {
        val idUsuario = UsuarioRepository.obtenerUsuarioLocal()!!.uid
        return TiendaAdquiridosRepository.obtenerMonedasUsuario(idUsuario).toString()
    }


    fun obtenerArticulosAdquiridos(idUsuario: Int): List<CompraArticulo> {
        return TiendaAdquiridosRepository.obtenerArticulosAdquiridosUsuario(idUsuario)

    }
}