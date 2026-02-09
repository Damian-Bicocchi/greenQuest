package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.database.TiendaAdquiridos

object TiendaAdquiridosRepository {

    private var tiendaUsuario : TiendaAdquiridos? = null
    private val tiendaDao by lazy {
        GreenQuestApp.instance.database.tiendaDao()
    }
    suspend fun crearTiendaParaUsuario(idUsuario: Int) : TiendaAdquiridos? {
        val tiendaAdquiridos = TiendaAdquiridos(
            userId = idUsuario,
            articulosAdquiridos = mutableListOf(),
            monedasObtenidas = 0
        )

        tiendaDao.insertarTienda(tiendaAdquiridos)
        tiendaUsuario = tiendaAdquiridos
        //addMonedas(10000,idUsuario) Para testing
        return  tiendaUsuario
    }

    suspend fun obtenerTiendaPorUsuario(idUsuario: Int): TiendaAdquiridos {
        val tiendaExistente = tiendaDao.obtenerTiendaPorUsuario(idUsuario)
        return if (tiendaExistente != null) {
            tiendaUsuario = tiendaExistente
            tiendaExistente
        } else {
            crearTiendaParaUsuario(idUsuario)!!
        }
    }

    suspend fun addMonedas(cantidadMonedas: Int, idUsuario: Int) {
        tiendaDao.addMonedas(cantidadMonedas, idUsuario)
        tiendaUsuario = tiendaDao.obtenerTiendaPorUsuario(idUsuario)
    }

    suspend fun comprarArticulo (cantidadMonedas: Int, idArticulo : Int, idUsuario: Int) : Boolean{
        val adquiridos = obtenerTiendaPorUsuario(idUsuario)
        if (adquiridos != null && adquiridos.monedasObtenidas >= cantidadMonedas){
            adquiridos.monedasObtenidas -= cantidadMonedas
            adquiridos.articulosAdquiridos.add(idArticulo)
            //Log.d("TIENDA", "Articulo ${idArticulo} comprado. Monedas restantes: ${adquiridos.monedasObtenidas} y ids Comprados ${adquiridos.articulosAdquiridos}")
            tiendaDao.actualizarTienda(adquiridos)
            tiendaUsuario = adquiridos
            return true
        }
        return false
    }

    suspend fun obtenerMonedasUsuario(idUsuario : Int) : Int {
        val adquiridos = obtenerTiendaPorUsuario(idUsuario)
        return adquiridos?.monedasObtenidas ?: 0
    }

    suspend fun obtenerArticulosAdquiridosUsuario(idUsuario : Int) : List<Int> {
        val adquiridos = obtenerTiendaPorUsuario(idUsuario)
        return adquiridos?.articulosAdquiridos ?: emptyList()
    }
    fun limpiarSesion() {
        tiendaUsuario = null
    }
}
