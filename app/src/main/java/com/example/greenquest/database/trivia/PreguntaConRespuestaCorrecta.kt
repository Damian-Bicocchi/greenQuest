package com.example.greenquest.database.trivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "pregunta_opcion",
    primaryKeys = ["pregunta_id", "opcion_id", "orden"],
    foreignKeys = [
        ForeignKey(
            entity = PreguntaTrivia::class,
            parentColumns = ["id"],
            childColumns = ["pregunta_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OpcionesTrivia::class,
            parentColumns = ["id"],
            childColumns = ["opcion_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PreguntaOpcion(
    @ColumnInfo(name = "pregunta_id", index = true)
    val preguntaId: Int,

    @ColumnInfo(name = "opcion_id", index = true)
    val opcionId: Int,

    @ColumnInfo(name = "es_correcta")
    val esCorrecta: Boolean = false
)
