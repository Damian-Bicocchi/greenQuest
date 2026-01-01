package com.example.greenquest.database.trivia

import com.google.gson.annotations.SerializedName

data class PreguntaJson(
    @SerializedName("questionText") val questionText: String,
    @SerializedName("options") val options: List<OpcionJson>
)