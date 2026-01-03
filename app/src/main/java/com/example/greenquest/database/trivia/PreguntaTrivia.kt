package com.example.greenquest.database.trivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preguntas")
data class PreguntaTrivia(
    @PrimaryKey(false) val preguntaId: Long,
    @ColumnInfo(name = "question_text") val questionText: String,
    @ColumnInfo(name = "answered_question") var answeredQuestion: Boolean = false,
)