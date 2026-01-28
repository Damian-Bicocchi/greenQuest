package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.estadisticas.ResumenPuntos
import com.example.greenquest.database.estadisticas.ResumenResiduo
import java.time.OffsetDateTime


@Dao
interface HistorialResiduoDao {

    @Query("""
        SELECT * FROM historial_residuos
        WHERE id_usuario = :idUsuario
        ORDER BY datetime(fecha) DESC
        LIMIT :cantDeseada
    """)
    suspend fun obtenerNResiduosMasRecientes(cantDeseada: Int, idUsuario: Int): List<HistorialResiduo>


    @Query("""
        SELECT * FROM historial_residuos
        WHERE id_usuario = :idUsuario
        ORDER BY datetime(fecha) DESC
    """)
    suspend fun obtenerTodosLosResiduos(idUsuario: Int): List<HistorialResiduo>


    @Query("""
        SELECT tipo_residuo, COUNT(*) as total 
        FROM historial_residuos
        WHERE (:fechaInicio IS NULL OR fecha >= :fechaInicio)
            AND (:fechaFin IS NULL OR fecha <= :fechaFin)
            AND id_usuario = :idUsuario
        GROUP BY tipo_residuo
    """)
    suspend fun obtenerResiduosEntreFechas(
        fechaInicio: OffsetDateTime?,
        fechaFin: OffsetDateTime?,
        idUsuario: Int
    ): List<ResumenResiduo>

    @Transaction
    @Insert
    suspend fun insertarResiduoAlHistorial(historialResiduo: HistorialResiduo)

    @Query("""
        SELECT date(fecha) as fecha, SUM(puntos_dados) as total
        FROM historial_residuos
        WHERE (:fechaInicio IS NULL OR fecha >= :fechaInicio)
            AND (:fechaFin IS NULL OR fecha <= :fechaFin)
            AND id_usuario = :idUsuario
        GROUP BY date(fecha)
        ORDER BY fecha ASC
    """)
    suspend fun obtenerPuntosEntreFechas(
        fechaInicio: OffsetDateTime?,
        fechaFin: OffsetDateTime?,
        idUsuario: Int
    ): List<ResumenPuntos>

    @Query("""
        SELECT SUM(puntos_dados) as total
        FROM historial_residuos
        WHERE (:fechaInicio IS NULL OR fecha >= :fechaInicio)
            AND (:fechaFin IS NULL OR fecha <= :fechaFin)
            AND id_usuario = :idUsuario
    """)
    suspend fun obtenerCantidadPuntosEntreFechas(
        fechaInicio: OffsetDateTime?,
        fechaFin: OffsetDateTime?,
        idUsuario: Int
    ): Int




}