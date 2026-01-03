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
                Log.e("triviaLogging", "La pregunta se dio. es ${pregunta.pregunta.questionText}")
                _preguntaActual.value = pregunta
                _gameState.value = EstadoTrivia.MOSTRANDO
            } else {
                Log.e("triviaLogging", "NO HAY PREGUNTAS EN LA BD")
                _gameState.value = EstadoTrivia.FINALIZADO
            }
        }
    }
}