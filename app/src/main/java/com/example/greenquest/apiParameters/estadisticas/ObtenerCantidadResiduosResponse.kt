package com.example.greenquest.apiParameters.estadisticas

import com.google.gson.annotations.SerializedName

@Deprecated("Sin uso en esta versión de greenQuest")
data class ObtenerCantidadResiduosResponse(
    val residuos: List<ResiduoItem>
)
