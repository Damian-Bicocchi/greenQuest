package com.example.greenquest.database.trivia

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class PreguntaConOpciones(
    @Embedded val pregunta: PreguntaTrivia,
    @Relation(
        parentColumn = "preguntaId",
        entityColumn = "preguntaCorrespondienteId"
    )
    val opciones: List<OpcionesTrivia>
)
