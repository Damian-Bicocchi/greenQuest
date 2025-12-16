package com.example.greenquest.apiParameters

import com.google.gson.annotations.SerializedName

enum class TipoResiduo(val value: String) {
    @SerializedName("Carton")
    CARTON("Carton"),

    @SerializedName("Plastico")
    PLASTICO("Plastico"),

    @SerializedName("Vidrio")
    VIDRIO("Vidrio"),

    @SerializedName("Metal")
    METAL("Metal"),

    @SerializedName("Papel")
    PAPEL("Papel"),

    @SerializedName("Basura")
    BASURA("Basura") // Categoría válida, pero no da puntos.
}