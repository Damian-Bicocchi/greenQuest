package com.example.greenquest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TipoResiduo(val nombre: String) {
    @SerialName("Papel")
    PAPEL("Papel"),
    @SerialName("Basura")
    BASURA("Basura"),
    @SerialName("Plastico")
    PLASTICO("Plástico"),
    @SerialName("Desconocido")
    DESCONOCIDO("Desconocido")
}