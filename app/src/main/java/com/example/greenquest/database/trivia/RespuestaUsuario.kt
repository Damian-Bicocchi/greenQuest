package com.example.greenquest.database.trivia

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ColumnInfo
import androidx.room.Index
import com.example.greenquest.database.user.User

@Entity(
    tableName = "respuestas_usuario",
    primaryKeys = ["userId", "preguntaId"],
    indices = [Index(value = ["preguntaId"])],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PreguntaTrivia::class,
            parentColumns = ["preguntaId"],
            childColumns = ["preguntaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RespuestaUsuario(
    val userId: Int,
    val preguntaId: Long,
    @ColumnInfo(name = "es_correcta") val esCorrecta: Boolean
)