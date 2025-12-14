package com.example.greenquest

import com.example.greenquest.apiParameters.TipoResiduo

data class ResiduoInfo(
    val id_residuo: String,
    val puntaje: Int,
    val tipo_residuo: TipoResiduo
)

