package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.database.tienda.CompraArticulo
import com.example.greenquest.database.tienda.TiendaAdquiridos

object TiendaAdquiridosRepository {

    private var tiendaUsuario : TiendaAdquiridos? = null
    private val tiendaDao by lazy {
        GreenQuestApp.instance.database.tiendaDao()
    }
    suspend fun crearTiendaParaUsuario(idUsuario: Int): TiendaAdquiridos {
        val monedasObtenidas = 10000 // Para testeo
        //val monedasObtenidas = 0
        val nuevaTienda = TiendaAdquiridos(
            userId = idUsuario,
            monedasObtenidas = monedasObtenidas
        )
        tiendaDao.insertarTienda(nuevaTienda)

        return nuevaTienda
    }

    suspend fun obtenerTiendaPorUsuario(idUsuario: Int): TiendaAdquiridos {
        val tiendaExistente = tiendaDao.obtenerTiendaPorUsuario(idUsuario)
        return if (tiendaExistente != null) {
            tiendaUsuario = tiendaExistente
            tiendaExistente
        } else {
            crearTiendaParaUsuario(idUsuario)
        }
    }

    suspend fun addMonedas(cantidadMonedas: Int, idUsuario: Int) {
        tiendaDao.addMonedas(cantidadMonedas, idUsuario)
        tiendaUsuario = tiendaDao.obtenerTiendaPorUsuario(idUsuario)
    }

    suspend fun comprarArticulo (cantidadMonedas: Int, idArticulo : Int, idUsuario: Int) : Boolean{
        val tiendaAdquiridos = obtenerTiendaPorUsuario(idUsuario)
        if (tiendaAdquiridos.monedasObtenidas >= cantidadMonedas){

            tiendaAdquiridos.monedasObtenidas -= cantidadMonedas


            val nuevaCompra = CompraArticulo(
                idCompraArticulo = 0,
                articuloId = idArticulo,
                adquirido = true,
                usuarioPropietarioId = idUsuario
            )

            tiendaDao.insertarCompra(nuevaCompra)
            Log.d("TIENDA", "Articulo ${idArticulo} comprado. " +
                    "Monedas restantes: ${tiendaAdquiridos.monedasObtenidas} ")


            tiendaDao.actualizarTienda(tiendaAdquiridos)
            tiendaUsuario = tiendaAdquiridos
            return true
        }
        return false
    }

    suspend fun obtenerMonedasUsuario(idUsuario : Int) : Int {
        val adquiridos = obtenerTiendaPorUsuario(idUsuario)
        return adquiridos.monedasObtenidas
    }

    suspend fun obtenerArticulosAdquiridosUsuario(idUsuario : Int) : List<Int> {
        return tiendaDao.obtenerIdsArticulosComprados(idUsuario)
    }
    fun limpiarSesion() {
        tiendaUsuario = null
    }
}
