package com.example.greenquest.apiParameters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TipoResiduo {
    @SerialName("Carton")
    CARTON,

    @SerialName("Plastico")
    PLASTICO,

    @SerialName("Vidrio")
    VIDRIO,

    @SerialName("Metal")
    METAL,

    @SerialName("Papel")
    PAPEL,

    @SerialName("Basura")
    BASURA
}
