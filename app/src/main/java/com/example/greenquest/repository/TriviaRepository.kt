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
import androidx.core.content.edit
import com.example.greenquest.Prefs

object TriviaRepository {
    private val triviaDao by lazy {
        GreenQuestApp.instance.database.triviaDao()
    }
    private lateinit var sharedPreferences: SharedPreferences

    private val triviaDataLoader = TriviaDataLoader()

    suspend fun inicializarData(context: Context){
        sharedPreferences = context.getSharedPreferences(Prefs(context = context).SHARED_NAME, 0)

        withContext(Dispatchers.IO){
            val versionTriviaApp = Prefs(context = context).getTriviaVersion()
            val versionTriviaJson = triviaDataLoader.getJsonVersion(context)
            if (versionTriviaJson == -1) return@withContext
            if (versionTriviaApp < versionTriviaJson){
                cargarDataDeJson(context = context)
                Prefs(context = context).saveTriviaVersion(versionTriviaJson)

            }
        }
    }

    suspend fun cargarDataDeJson(context : Context) {
        withContext(Dispatchers.IO) {
            try {
                val triviaMetadata = triviaDataLoader.loadTriviaDataFromJson(context = context)

                triviaMetadata.questions.forEach { preguntaJson ->
                    val preguntaBD = triviaDao.obtenerPreguntaConOpcionesPorId(preguntaJson.questionId)
                    var preguntaId : Long
                    if (preguntaBD == null){
                        // No hay pregunta por lo que agregala
                        preguntaId = triviaDao.insertarPregunta(
                            PreguntaTrivia(
                                preguntaId = preguntaJson.questionId,
                                questionText = preguntaJson.questionText,
                                answeredQuestion = false
                            )
                        )
                    } else {
                        preguntaId = preguntaBD.pregunta.preguntaId
                        triviaDao.actualizarPregunta(
                            PreguntaTrivia(
                                preguntaId = preguntaJson.questionId,
                                questionText = preguntaJson.questionText,
                                answeredQuestion = preguntaBD.pregunta.answeredQuestion
                            )
                        )
                        // Si la pregunta ya fue respondida, no le cambies el estado ^
                    }

                    Log.e("triviaLogging", "Las opciones segun el json son" + preguntaJson.options)

                    val opciones = preguntaJson.options.mapIndexed { _, opcionJson ->
                        OpcionesTrivia(
                            opcionId = 0,
                            preguntaCorrespondienteId = preguntaId,
                            textoOpcion = opcionJson.textoOpcion,
                            esCorrecta = opcionJson.esCorrecta,
                        )
                    }
                    Log.d("triviaLogging",
                        (opciones + "dale esto se cargo en opciones linea 71").toString()
                    )

                    triviaDao.insertarOpciones(opciones)
                }
                Log.e("triviaLogging", "Preguntas cargadas")
            } catch (e: Exception) {
                Log.e("triviaLogging", "Error cargando datos desde JSON ${e}")
            }
        }
    }

    suspend fun obtenerPreguntaAleatoria(): PreguntaConOpciones? {
        val pregunta = triviaDao.obtenerPreguntaConOpcionesCompleta()
        return pregunta
    }

    suspend fun marcarComoRespondida(preguntaId: Long) {
        val preguntaParaActualizar = triviaDao.obtenerPreguntaConOpcionesPorId(preguntaId)
        preguntaParaActualizar?.pregunta?.let {
            it.answeredQuestion = true
            triviaDao.actualizarPregunta(it)
        }

    }

    suspend fun chequearRespuesta(idPregunta: Long, idRespuesta: Long) : Boolean{
        val pregunta = triviaDao.obtenerPreguntaConOpcionesPorId(idPregunta) ?: return false
        return pregunta.opciones?.any {
            opcion ->
            (opcion.opcionId== idRespuesta) && opcion.esCorrecta
        } ?: false


    }


}