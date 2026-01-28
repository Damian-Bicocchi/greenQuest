package com.example.greenquest.apiParameters.estadisticas

import com.google.gson.annotations.SerializedName

data class ObtenerCantidadResiduosRequest(
    @SerializedName("id_usuario")
    val idUsuario : Int
)

