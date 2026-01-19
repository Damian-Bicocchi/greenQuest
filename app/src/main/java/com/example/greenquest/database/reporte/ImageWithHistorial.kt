package com.example.greenquest.database.reporte

import androidx.room.Embedded
import androidx.room.Relation
import com.example.greenquest.database.estadisticas.HistorialResiduo

data class ImageWithHistorial(
    @Embedded
    val image: ImageModel,

    @Relation(
        parentColumn = "id_historial_residuo_reportado",
        entityColumn = "historialResiduoId"
    )
    val historial: HistorialResiduo
)
