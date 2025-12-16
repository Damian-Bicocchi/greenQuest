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
    PLASTICO("Plastico"),

    @SerialName("Metal")
    METAL("Metal"),

    @SerialName("Vidrio")
    VIDRIO("Vidrio"),

    @SerialName("Carton")
    CARTON("Carton"),

    @SerialName("Desconocido")
    DESCONOCIDO("Desconocido")
}