package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia
import com.example.greenquest.database.trivia.RespuestaUsuario

@Dao
interface TriviaDao {


    @Query("""
    SELECT * FROM preguntas 
    WHERE preguntaId NOT IN (
        SELECT preguntaId 
        FROM respuestas_usuario 
        WHERE userId = :currentUserId AND es_correcta = 1
    )
    ORDER BY RANDOM() 
    LIMIT 1
""")
    suspend fun obtenerPreguntaAleatoria(currentUserId: Int): PreguntaTrivia?


    @Query("SELECT * FROM opciones WHERE preguntaCorrespondienteId = :preguntaId")
    suspend fun obtenerOpcionesPorPreguntaId(preguntaId: Long): List<OpcionesTrivia>


    @Transaction
    suspend fun obtenerPreguntaConOpcionesCompleta(currentUserId: Int): PreguntaConOpciones? {
        val pregunta = obtenerPreguntaAleatoria(currentUserId = currentUserId) ?: return null

        val opciones = obtenerOpcionesPorPreguntaId(pregunta.preguntaId)
        return PreguntaConOpciones(pregunta, opciones)
    }

    @Transaction
    @Query("SELECT * FROM preguntas WHERE preguntaId = :id")
    suspend fun obtenerPreguntaConOpcionesPorId(id: Long): PreguntaConOpciones?

    @Transaction
    @Update
    suspend fun actualizarPregunta(pregunta: PreguntaTrivia) : Int

    @Transaction
    @Insert
    suspend fun insertarPregunta(pregunta: PreguntaTrivia): Long

    @Transaction
    @Insert
    suspend fun insertarOpciones(opciones: List<OpcionesTrivia>)

    @Query("SELECT COUNT(*) FROM opciones WHERE preguntaCorrespondienteId = :preguntaId")
    suspend fun contarOpciones(preguntaId: Long): Int

    @Query("SELECT explanation FROM preguntas WHERE preguntaId = :preguntaId")
    suspend fun getExplicacionPregunta(preguntaId: Long) : String?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRespuesta(respuesta: RespuestaUsuario)



}