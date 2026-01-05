package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.repository.TriviaRepository
import com.example.greenquest.states.trivia.EstadoTrivia
import kotlinx.coroutines.launch

class TriviaViewModel: ViewModel() {

    private val _preguntaActual = MutableLiveData<PreguntaConOpciones?>()
    val preguntaActual: LiveData<PreguntaConOpciones?> = _preguntaActual

    private val _gameState = MutableLiveData(EstadoTrivia.CARGANDO)
    val gameState: LiveData<EstadoTrivia> = _gameState



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

    fun chequearRespuestaCorrecta(idPregunta: Long, idRespuesta: Long){
        viewModelScope.launch {

            val correcta =  TriviaRepository.chequearRespuesta(
                idPregunta = idPregunta, idRespuesta = idRespuesta)
            Log.d("triviaLogging", "Estoy en chequearRespuestaCorrecta y fue ${correcta}")

            if (correcta){
                _gameState.value = EstadoTrivia.CORRECTO
            } else {
                _gameState.value = EstadoTrivia.INCORRECTO
            }
        }


    }
}