package com.example.greenquest.database.estadisticas

import com.example.greenquest.apiParameters.TipoResiduo

data class ResumenResiduo(
    val tipo_residuo: TipoResiduo,
    val total: Int
)
