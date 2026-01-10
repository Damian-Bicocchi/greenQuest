package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.greenquest.database.estadisticas.HistorialResiduo


@Dao
interface HistorialResiduoDao {

    @Transaction
    @Query("""
        SELECT * FROM historial_residuos
        WHERE id_usuario = :idUsuario
        ORDER BY datetime(fecha) DESC
        LIMIT :cantDeseada
    """)
    suspend fun obtenerNResiduosMasRecientes(cantDeseada: Int, idUsuario: Int): List<HistorialResiduo>


    @Transaction
    @Query("""
        SELECT * FROM historial_residuos
        WHERE id_usuario = :idUsuario
        ORDER BY datetime(fecha) DESC
    """)
    suspend fun obtenerTodosLosResiduos(idUsuario: Int): List<HistorialResiduo>

    @Transaction
    @Query("""
        SELECT * FROM historial_residuos
        WHERE (:fechaInicio IS NULL OR fecha >= :fechaInicio)
          AND (:fechaFin IS NULL OR fecha <= :fechaFin)
          AND (id_usuario = :idUsuario)
        ORDER BY datetime(fecha) DESC
    """)
    suspend fun obtenerTodosLosResiduosEntreFechas(
        fechaInicio: String?,
        fechaFin: String?,
        idUsuario: Int
    ): List<HistorialResiduo>

    @Transaction
    @Insert
    suspend fun insertarResiduoAlHistorial(historialResiduo: HistorialResiduo)



}