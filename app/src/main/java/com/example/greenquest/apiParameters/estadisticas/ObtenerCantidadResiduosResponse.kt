package com.example.greenquest.apiParameters.estadisticas

import com.google.gson.annotations.SerializedName

data class ObtenerCantidadResiduosResponse(
    val residuos: List<ResiduoItem>
)
