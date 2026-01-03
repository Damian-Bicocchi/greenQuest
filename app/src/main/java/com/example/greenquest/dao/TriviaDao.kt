package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia

@Dao
interface TriviaDao {

    @Transaction
    @Query(
        """
        SELECT * FROM preguntas 
        WHERE answered_question = 0
        ORDER BY RANDOM() 
        LIMIT 1
    """
    )
    suspend fun obtenerPreguntaNoRespondidaConOpciones(): PreguntaConOpciones?

    @Transaction
    @Query("SELECT * FROM preguntas WHERE preguntaId = :id")
    suspend fun obtenerPreguntaConOpcionesPorId(id: Long): PreguntaConOpciones?

    @Update
    suspend fun actualizarPregunta(pregunta: PreguntaTrivia) : Int

    @Insert
    suspend fun insertarPregunta(pregunta: PreguntaTrivia): Long

    @Insert
    suspend fun insertarOpciones(opciones: List<OpcionesTrivia>)


}