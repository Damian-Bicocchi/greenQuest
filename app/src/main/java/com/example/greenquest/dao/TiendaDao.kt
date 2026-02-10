package com.example.greenquest.dao

import androidx.room.*
import com.example.greenquest.database.tienda.CompraArticulo
import com.example.greenquest.database.tienda.TiendaAdquiridos

@Dao
interface TiendaDao {


    @Query("SELECT * FROM tiendaadquiridos WHERE userId = :idUsuario LIMIT 1")
    suspend fun obtenerTiendaPorUsuario(idUsuario: Int): TiendaAdquiridos?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTienda(tiendaAdquiridos: TiendaAdquiridos)

    @Update
    suspend fun actualizarTienda(tiendaAdquiridos: TiendaAdquiridos)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCompra(compra: CompraArticulo): Long

    @Query("SELECT articuloId FROM compras WHERE usuarioPropietarioId = :idUsuario")
    suspend fun obtenerIdsArticulosComprados(idUsuario: Int): List<Int>


    @Query("UPDATE tiendaadquiridos SET monedas_obtenidas = monedas_obtenidas + :cantidadMonedas WHERE userId = :idUsuario")
    suspend fun addMonedas(cantidadMonedas: Int, idUsuario: Int)

}