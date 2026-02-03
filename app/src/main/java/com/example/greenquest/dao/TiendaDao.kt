package com.example.greenquest.dao

import androidx.room.*
import com.example.greenquest.database.TiendaAdquiridos

@Dao
interface TiendaDao {


    @Query("SELECT * FROM tiendaadquiridos WHERE userId = :idUsuario LIMIT 1")
    suspend fun obtenerTiendaPorUsuario(idUsuario: Int): TiendaAdquiridos?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTienda(tiendaAdquiridos: TiendaAdquiridos)

    @Update
    suspend fun actualizarTienda(tiendaAdquiridos: TiendaAdquiridos)



    @Query("UPDATE tiendaadquiridos SET monedas_obtenidas = monedas_obtenidas + :cantidadMonedas WHERE userId = :idUsuario")
    suspend fun addMonedas(cantidadMonedas: Int, idUsuario: Int)

}