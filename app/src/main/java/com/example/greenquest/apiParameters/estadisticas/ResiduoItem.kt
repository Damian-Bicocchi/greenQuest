package com.example.greenquest.apiParameters.estadisticas

import com.google.gson.annotations.SerializedName

data class ResiduoItem(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName(value = "cantidad")
    val cantidad: Int,
)
