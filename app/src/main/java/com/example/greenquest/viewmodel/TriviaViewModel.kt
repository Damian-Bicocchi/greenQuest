package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.repository.TriviaRepository
import com.example.greenquest.states.trivia.EstadoTrivia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.exp

class TriviaViewModel: ViewModel() {

    private val _preguntaActual = MutableLiveData<PreguntaConOpciones?>()
    val preguntaActual: LiveData<PreguntaConOpciones?> = _preguntaActual

    private val _gameState = MutableLiveData(EstadoTrivia.CARGANDO)
    val gameState: LiveData<EstadoTrivia> = _gameState

    private val _explicacionState = MutableStateFlow<String?>(null)
    val explicacionState: StateFlow<String?> = _explicacionState





    fun loadNextQuestion() {
        _gameState.value = EstadoTrivia.CARGANDO

        viewModelScope.launch {
            val pregunta = TriviaRepository.obtenerPreguntaAleatoria()

            if (pregunta != null) {
                _preguntaActual.value = pregunta
                _gameState.value = EstadoTrivia.MOSTRANDO
            } else {
                _gameState.value = EstadoTrivia.FINALIZADO
            }
        }
    }

    fun chequearRespuestaCorrecta(idPregunta: Long, idRespuesta: Long) {
        viewModelScope.launch {
            try {
                val esCorrecta = TriviaRepository.chequearRespuesta(idPregunta, idRespuesta)
                val explicacionCargada = try {
                    TriviaRepository.getExplicacion(idPregunta)
                } catch (_: Exception) {
                    "No se pudo cargar la explicación."
                }

                _explicacionState.value = explicacionCargada

                if (esCorrecta) {
                    TriviaRepository.marcarComoRespondida(idPregunta)
                    _gameState.value = EstadoTrivia.CORRECTO
                } else {
                    _gameState.value = EstadoTrivia.INCORRECTO
                }

            } catch (e: Exception) {
                Log.e("triviaLogging", "Error: ${e.message}")
            }
        }
    }

    fun getExplicacion(idPregunta: Long) {
        viewModelScope.launch {
            try {
                val explicacion = TriviaRepository.getExplicacion(idPregunta = idPregunta)
                _explicacionState.value = explicacion
            } catch (e: Exception) {
                _explicacionState.value = "Error al obtener explicación"
                Log.e("triviaLogging", "Error: ${e.message}")
            }
        }
    }

    fun resetExplicacion() {
        _explicacionState.value = null
    }
}