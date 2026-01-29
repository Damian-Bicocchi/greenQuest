package com.example.greenquest.database.estadisticas

import androidx.room.ColumnInfo
import com.example.greenquest.apiParameters.TipoResiduo

data class ResumenResiduo(
    @ColumnInfo(name = "tipo_residuo") val tipoResiduo: TipoResiduo,
    @ColumnInfo(name = "total") val total: Int
)
