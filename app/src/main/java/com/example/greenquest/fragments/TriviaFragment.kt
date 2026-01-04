package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.databinding.FragmentTriviaBinding
import com.example.greenquest.states.trivia.EstadoTrivia
import com.example.greenquest.viewmodel.TriviaViewModel


class TriviaFragment : Fragment() {

    private lateinit var triviaViewModel: TriviaViewModel
    private var selectedOptionId: Long? = null
    private var selectedQuestionId: Long? = null
    private var _binding: FragmentTriviaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        triviaViewModel = ViewModelProvider(this)[TriviaViewModel::class.java]
    }

    private fun showLoading(bool: Boolean) {
        Log.d("triviaLogging","Pregunta lista para ser mostrada")
    }

    private fun showGameFinished() {
        Log.e("greenQuest", "voy a cambiar las cosas")
        binding.triviaPreguntaTexto.text = "Impresionante"
        binding.containerOpciones.visibility = View.INVISIBLE
        binding.botonResponder.visibility = View.INVISIBLE
    }

    private fun mostrarPregunta(preguntaConOpciones: PreguntaConOpciones){
        binding.triviaPreguntaTexto.text = preguntaConOpciones.pregunta.questionText
        binding.containerOpciones.removeAllViews()
        val letters = mutableListOf<Char>()
        for (letra in 'A'..'Z') {
            letters.add(letra)
        }
        val radioGroup : RadioGroup = binding.containerOpciones


        preguntaConOpciones.opciones?.forEachIndexed { index, opcion ->
            val letraChar = letters.getOrElse(index = index) {'?'}
            val radioButton = RadioButton(requireContext())

            radioButton.id = View.generateViewId()
            radioButton.text = letraChar + " " + opcion.textoOpcion
            radioGroup.addView(radioButton)
            radioButton.setOnClickListener { onOptionSelected(opcion.opcionId, it) }
        }
    }

    private fun onOptionSelected(opcionId: Long, optionView: View) {
        optionView.setBackgroundColor(0)
        binding.containerOpciones.children.map {
            opcion -> opcion.setBackgroundColor(12)
        }


        selectedOptionId = opcionId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTriviaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showEstadoRespuesta(esCorrecta: Boolean){
        Toast.makeText(requireContext(),
            "La respuesta es ${esCorrecta}",
            Toast.LENGTH_LONG).show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        triviaViewModel.preguntaActual.observe(this) { pregunta ->

            pregunta?.let {
                selectedQuestionId = pregunta.pregunta.preguntaId
                this.mostrarPregunta(it) }
        }

        // Observar estado del juego
        triviaViewModel.gameState.observe(this) { state ->
            when (state) {
                EstadoTrivia.CARGANDO -> showLoading(true)
                EstadoTrivia.MOSTRANDO -> {showLoading(false)}
                EstadoTrivia.FINALIZADO -> showGameFinished()
                EstadoTrivia.CORRECTO -> showEstadoRespuesta(true)
                EstadoTrivia.INCORRECTO -> showEstadoRespuesta(false)
            }
        }

        binding.botonResponder.setOnClickListener {
            triviaViewModel.chequearRespuestaCorrecta(
                selectedQuestionId!!,
                selectedOptionId!!)
        }

        triviaViewModel.loadNextQuestion()
    }

}