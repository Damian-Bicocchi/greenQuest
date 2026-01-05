package com.example.greenquest.database.trivia

import com.google.gson.annotations.SerializedName

data class TriviaMetadata(
    @SerializedName("version") val version: Int,
    @SerializedName("ultimaActualizacion") val lastUpdated: String,
    @SerializedName("preguntas") val questions: List<PreguntaJson>
)