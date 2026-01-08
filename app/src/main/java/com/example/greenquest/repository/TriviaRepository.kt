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
import com.example.greenquest.Prefs
import com.example.greenquest.database.trivia.RespuestaUsuario


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
                                preguntaJson.explanation
                            )
                        )
                    } else {
                        preguntaId = preguntaBD.pregunta.preguntaId
                        triviaDao.actualizarPregunta(
                            PreguntaTrivia(
                                preguntaId = preguntaJson.questionId,
                                questionText = preguntaJson.questionText,
                                preguntaJson.explanation
                            )
                        )
                        // Si la pregunta ya fue respondida, no le cambies el estado ^
                    }
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
            } catch (e: Exception) {
                Log.e("greenQuest", "Error cargando datos desde JSON ${e}")
            }
        }
    }

    suspend fun obtenerPreguntaAleatoria(): PreguntaConOpciones? {
        val pregunta = triviaDao.obtenerPreguntaConOpcionesCompleta(UsuarioRepository.obtenerUsuarioLocal()!!.uid)
        return pregunta
    }

    suspend fun marcarComoRespondida(preguntaId: Long, userId: Int, esCorrecta: Boolean) {
        triviaDao.insertRespuesta(RespuestaUsuario(
            preguntaId = preguntaId,
            userId = userId,
            esCorrecta = esCorrecta
        ))
    }

    suspend fun chequearRespuesta(idPregunta: Long, idRespuesta: Long) : Boolean{
        val pregunta = triviaDao.obtenerPreguntaConOpcionesPorId(idPregunta) ?: return false
        return pregunta.opciones?.any {
            opcion ->
            (opcion.opcionId == idRespuesta) && opcion.esCorrecta
        } ?: false
    }

    suspend fun getExplicacion(idPregunta: Long): String {
        return triviaDao.obtenerPreguntaConOpcionesPorId(idPregunta)?.pregunta?.explanation
            ?: ""
    }


}