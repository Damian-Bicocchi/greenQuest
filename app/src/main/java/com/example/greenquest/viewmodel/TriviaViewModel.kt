package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.repository.TriviaRepository
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.states.trivia.EstadoTrivia
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TriviaViewModel: ViewModel() {

    private val _preguntaActual = MutableLiveData<PreguntaConOpciones?>()
    val preguntaActual: LiveData<PreguntaConOpciones?> = _preguntaActual

    private val _gameState = MutableLiveData(EstadoTrivia.CARGANDO)
    val gameState: LiveData<EstadoTrivia> = _gameState

    private val _explicacionState = MutableStateFlow<String?>(null)
    val explicacionState: StateFlow<String?> = _explicacionState

    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO


    fun loadNextQuestion() {
        _gameState.value = EstadoTrivia.CARGANDO
        viewModelScope.launch {
            val preguntaDeferred = async(dispatcherIO){
                TriviaRepository.obtenerPreguntaAleatoria()
            }
            val pregunta = preguntaDeferred.await()
            if (pregunta != null) {
                _preguntaActual.value = pregunta
                _gameState.value = EstadoTrivia.MOSTRANDO
            } else {
                _gameState.value = EstadoTrivia.FINALIZADO
            }
        }
    }

    fun chequearRespuestaCorrecta(idPregunta: Long, idRespuesta: Long) {
        viewModelScope.launch{
            try {
                val esCorrectaDeferred = async(dispatcherIO) {
                    TriviaRepository.chequearRespuesta(idPregunta = idPregunta, idRespuesta = idRespuesta)
                }

                val explicacionDeferred = async(dispatcherIO) {
                    try {
                        TriviaRepository.getExplicacion(idPregunta = idPregunta)
                    } catch (e: Exception) {
                        Log.e("greenQuest", "Excepción en chequearRespuestaCorrecta ${e.toString()}")
                        "No se pudo cargar la explicación"
                    }
                }

                val esCorrecta = esCorrectaDeferred.await()
                val explicacionCargada = explicacionDeferred.await()

                _explicacionState.value = explicacionCargada
                if (esCorrecta) {
                    _gameState.value = EstadoTrivia.CORRECTO
                } else {
                    _gameState.value = EstadoTrivia.INCORRECTO
                }
                withContext(dispatcherIO){
                    TriviaRepository.marcarComoRespondida(
                        idPregunta,
                        userId = UsuarioRepository.obtenerUsuarioLocal()!!.uid,
                        esCorrecta = esCorrecta)
                }
            } catch (e: Exception) {
                Log.e("greenQuest", "Error en chequearRespuestaCorrecta: ${e.message}")
            }
        }
    }
}