package com.example.greenquest.database.trivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preguntas")
data class PreguntaTrivia(
    @PrimaryKey(autoGenerate = true) val preguntaId: Int,
    @ColumnInfo(name = "question_text") val questionText: String,
    @ColumnInfo(name = "answered_question")  val answeredQuestion: Boolean = false,
)