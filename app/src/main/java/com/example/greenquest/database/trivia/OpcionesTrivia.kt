package com.example.greenquest.database.trivia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opciones")
data class OpcionesTrivia(
    @PrimaryKey(autoGenerate = true) val opcionId: Long,
    val preguntaCorrespondienteId: Long,
    val esCorrecta: Boolean,
    val textoOpcion: String,
)
