package com.example.greenquest.apiParameters.scanning

import com.google.gson.annotations.SerializedName

data class ReclamarResiduoGenericResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("error")
    val error: String? = null
)
