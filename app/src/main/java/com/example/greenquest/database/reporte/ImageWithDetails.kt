package com.example.greenquest.database.reporte

import androidx.room.Embedded
import androidx.room.Relation
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.user.User



data class ImageWithDetails(
    @Embedded
    val image: ImageModel,

    @Relation(
        parentColumn = "id_historial_residuo_reportado",
        entityColumn = "historialResiduoId"
    )
    val historial: HistorialResiduo,

    @Relation(
        parentColumn = "id_usuario_reporte",
        entityColumn = "uid"
    )
    val usuario: User
)

