package com.example.greenquest.database.estadisticas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.states.reporte.EstadoReporte
import java.time.OffsetDateTime

@Entity(tableName = "historial_residuos")
data class HistorialResiduo(
    @PrimaryKey(autoGenerate = true) val historialResiduoId: Long,
    @ColumnInfo(name = "id_residuo") val idResiduo: String,
    @ColumnInfo(name = "id_usuario") val idUsuario: Int,
    @ColumnInfo(name = "fecha") val fecha: OffsetDateTime? = null,
    @ColumnInfo(name = "tipo_residuo") val tipoResiduo: TipoResiduo,
    @ColumnInfo(name = "puntos_dados") val puntosDados: Int,
    @ColumnInfo(name = "estado") val estadoReporte: EstadoReporte = EstadoReporte.SINREPORTE
)
