package com.example.greenquest

import com.example.greenquest.apiParameters.TipoResiduo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrPayloadResiduo(
    @SerialName("ID Residuo")
    val id_residuo: String,
    @SerialName("Puntos")
    val puntaje: Int,
    @SerialName("Tipo Residuo")
    val tipo_residuo: TipoResiduo
)

