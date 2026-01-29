package com.example.greenquest.database.estadisticas

import com.example.greenquest.apiParameters.TipoResiduo

data class ResumenResiduo(
    val tipoResiduo: TipoResiduo,
    val total: Int
)
