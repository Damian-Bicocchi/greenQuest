package com.example.greenquest.database.trivia

import com.google.gson.annotations.SerializedName

data class OpcionJson(
    @SerializedName("textoOpcion") val textoOpcion: String,
    @SerializedName("esCorrecta") val esCorrecta: Boolean
)