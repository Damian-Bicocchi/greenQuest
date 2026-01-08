package com.example.greenquest.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
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


    private fun showGameFinished() {
        binding.triviaPreguntaTexto.text = "Impresionante. Respondiste bien todas las preguntas"
        binding.containerOpciones.visibility = View.INVISIBLE
        binding.botonResponder.visibility = View.INVISIBLE
    }

    private fun mostrarPregunta(preguntaConOpciones: PreguntaConOpciones){
        binding.triviaPreguntaTexto.text = preguntaConOpciones.pregunta.questionText
        binding.containerOpciones.removeAllViews()

        val radioGroup : RadioGroup = binding.containerOpciones


        preguntaConOpciones.opciones?.forEachIndexed { index, opcion ->
            val radioButton = RadioButton(requireContext())
            radioButton.id = View.generateViewId()
            radioButton.text = opcion.textoOpcion
            radioGroup.addView(radioButton)
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)

            radioButton.setOnClickListener { onOptionSelected(opcion.opcionId, it) }

        }


    }

    private fun onOptionSelected(opcionId: Long, optionView: View) {
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

        val explicacion = triviaViewModel.explicacionState.value ?: ""
        val titulo = if (esCorrecta) "\uD83D\uDC4C\u200B\uD83D\uDC4C\u200B Muy bien \uD83D\uDC4C\u200B\uD83D\uDC4C\u200B" else "❌\u200B❌\u200B"
        val mensaje = if (esCorrecta) "Excelente: $explicacion" else explicacion
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setMessage(mensaje)
            .setTitle(titulo)

            .setPositiveButton("Entendido") { _, _ ->
                triviaViewModel.loadNextQuestion()
            }
            .setOnCancelListener {
                triviaViewModel.loadNextQuestion()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        triviaViewModel.preguntaActual.observe(viewLifecycleOwner) { pregunta ->
            pregunta?.let {
                selectedQuestionId = pregunta.pregunta.preguntaId
                this.mostrarPregunta(it) }
        }

        // Observar estado del juego
        triviaViewModel.gameState.observe(viewLifecycleOwner) { state ->
            when (state) {
                EstadoTrivia.CARGANDO -> {}
                EstadoTrivia.MOSTRANDO -> {}
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