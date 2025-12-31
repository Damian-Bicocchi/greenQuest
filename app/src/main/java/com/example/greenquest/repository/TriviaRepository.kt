package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.database.trivia.PreguntaConOpciones

object TriviaRepository {
    private val triviaDao by lazy {
        GreenQuestApp.instance.database.triviaDao()
    }

    suspend fun obtenerPreguntaAleatoria(): PreguntaConOpciones? {
        return triviaDao.obtenerPreguntaNoRespondidaConOpciones()
    }

    suspend fun marcarComoRespondida(preguntaId: Long) {
        var preguntaParaActualizar = triviaDao.obtenerPreguntaConOpcionesPorId(preguntaId)
        preguntaParaActualizar?.pregunta?.let {
            it?.answeredQuestion = true
            triviaDao.actualizarPregunta(it)
        }

    }

    suspend fun chequearRespuesta(idPregunta: Long, idRespuesta: Long) : Boolean{
        val pregunta = triviaDao.obtenerPreguntaConOpcionesPorId(idPregunta) ?: return false
        val opcion = pregunta.opciones.stream().filter { (opcionId, _, esCorrecta) -> (opcionId == idRespuesta) && esCorrecta}

        return (opcion != null)
    }


}