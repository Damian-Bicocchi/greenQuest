package com.example.greenquest.database.trivia

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opciones")
data class OpcionesTrivia(
    @PrimaryKey(autoGenerate = true) val opcionId: Long,
    @ColumnInfo(name = "option_text") val optionText: String
)
