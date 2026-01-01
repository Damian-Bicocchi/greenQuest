package com.example.greenquest.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.database.trivia.OpcionesTrivia
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.database.trivia.PreguntaTrivia
import com.example.greenquest.database.trivia.TriviaDataLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object TriviaRepository {
    private val triviaDao by lazy {
        GreenQuestApp.instance.database.triviaDao()
    }
    private lateinit var sharedPreferences: SharedPreferences
    private val DATA_VERSION_KEY = "triviaVersion"

    private val triviaDataLoader = TriviaDataLoader()

    private lateinit var context: Context

    suspend fun inicializarData(context: Context){
        this.context = context
        withContext(Dispatchers.IO){
            val versionTriviaApp = sharedPreferences.getInt(DATA_VERSION_KEY, 0)
            val versionTriviaJson = triviaDataLoader.getJsonVersion(context)

            if (versionTriviaApp < versionTriviaJson){
                cargarDataDeJson()

                sharedPreferences.edit().putInt(DATA_VERSION_KEY, versionTriviaJson).apply()

            }
        }
    }

    suspend fun cargarDataDeJson() {
        withContext(Dispatchers.IO) {
            try {
                val triviaMetadata = triviaDataLoader.loadTriviaDataFromJson(context = context)

                // ACA deberia haber logica para cuando se actualicen las preguntas y ya las haya respondido
                // 1. Se debe cambiar las clases y la bd para que la pregunta en el json tenga su id, asi poder chequear y no pisar
                //      el booleano de ya respondido
                // 2. Chequear el texto de la pregunta antes de actualizarla, pero no funcionaria si cambio el texto en el nuevo json


                triviaMetadata.questions.forEach { preguntaJson ->

                    val preguntaId = triviaDao.insertarPregunta(
                        PreguntaTrivia(
                            preguntaId = 0,
                            questionText = preguntaJson.questionText,
                            answeredQuestion = false
                        )
                    )

                    val opciones = preguntaJson.options.mapIndexed { _, opcionJson ->
                        OpcionesTrivia(
                            opcionId = 0,
                            preguntaCorrespondienteId = preguntaId,
                            textoOpcion = opcionJson.textoOpcion,
                            esCorrecta = opcionJson.esCorrecta,
                        )
                    }

                    triviaDao.insertarOpciones(opciones)


                }
                Log.e("greenQuest", "Preguntas cargadas")
            } catch (e: Exception) {
                Log.e("greenQuest", "Error cargando datos desde JSON ${e}",)

            }
        }
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