package com.example.greenquest.database.estadisticas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.greenquest.apiParameters.TipoResiduo
import java.time.OffsetDateTime

@Entity(tableName = "historial_residuos")
data class HistorialResiduo(
    @PrimaryKey(autoGenerate = true) val historialResiduoId: Long,
    @ColumnInfo(name = "fecha") val fecha: OffsetDateTime? = null,
    @ColumnInfo(name = "tipo_residuo") val tipoResiduo: TipoResiduo,
    @ColumnInfo(name = "puntos_dados") val puntosDados: Int,
)
