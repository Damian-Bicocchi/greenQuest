package com.example.greenquest.apiParameters.scanning

import com.google.gson.annotations.SerializedName

data class ReclamarResiduoRequest(
    @SerializedName("id_residuo")
    val idResiduo: String
)
