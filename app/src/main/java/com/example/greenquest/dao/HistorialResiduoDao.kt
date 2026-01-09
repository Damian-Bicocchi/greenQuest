package com.example.greenquest.dao

import androidx.room.Query
import com.example.greenquest.database.estadisticas.HistorialResiduo

interface HistorialResiduoDao {


    @Query("""
        SELECT * FROM historial_residuos
        ORDER BY datetime(fecha) DESC
        LIMIT :cantDeseada
    """)
    suspend fun obtenerNResiduosMasRecientes(cantDeseada: Int): List<HistorialResiduo?>


    @Query("""
        SELECT * FROM historial_residuos
        ORDER BY datetime(fecha) DESC
    """)
    suspend fun obtenerTodosLosResiduos(): List<HistorialResiduo>

    @Query("""
        SELECT * FROM historial_residuos
        WHERE (:fechaInicio IS NULL OR fecha >= :fechaInicio)
          AND (:fechaFin IS NULL OR fecha <= :fechaFin)
        ORDER BY datetime(fecha) DESC
    """)
    suspend fun obtenerTodosLosResiduosEntreFechas(
        fechaInicio: String?,
        fechaFin: String?
    ): List<HistorialResiduo>



}