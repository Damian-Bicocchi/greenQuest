package com.example.greenquest.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.R
import com.example.greenquest.database.trivia.PreguntaConOpciones
import com.example.greenquest.databinding.FragmentTriviaBinding
import com.example.greenquest.states.trivia.EstadoTrivia
import com.example.greenquest.viewmodel.TriviaViewModel


class TriviaFragment : Fragment() {

    private lateinit var triviaViewModel: TriviaViewModel
    private var selectedOptionId: Long? = null
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

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun mostrarPregunta(preguntaConOpciones: PreguntaConOpciones){
        binding.triviaPreguntaTexto.text = preguntaConOpciones.pregunta.questionText
        binding.containerOpciones.removeAllViews()
        preguntaConOpciones.opciones?.forEachIndexed { index, opcion ->
            val optionView = LayoutInflater.from(requireContext())
                .inflate(R.layout.opcion_trivia,
                    binding.containerOpciones,
                    false)


            val letters = mutableListOf<Char>()
            for (letra in 'A'..'Z') {
                letters.add(letra)
            }

            val letraChar: Char = letters.getOrElse(index) { '?' }
            optionView.findViewById<TextView>(R.id.tv_option_letter).text = letraChar.toString()

            optionView.findViewById<TextView>(R.id.tv_option_text).text = opcion.textoOpcion
            // Marcar como seleccionada
            optionView.setOnClickListener {
                Log.d("triviaLogging", "En linea 65")
                onOptionSelected(opcion.opcionId, optionView)
            }

            // Agregar al contenedor
            binding.containerOpciones.addView(optionView)
        }
    }

    private fun onOptionSelected(opcionId: Long, optionView: View) {
        optionView.setBackgroundColor(0)
        binding.containerOpciones.children.map {
            opcion -> opcion.setBackgroundColor(12)
        }

        Log.d("triviaLogging", "Eligio la opcion ${opcionId}")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        triviaViewModel.preguntaActual.observe(this) { pregunta ->
            pregunta?.let { mostrarPregunta(it) }
        }

        // Observar estado del juego
        triviaViewModel.gameState.observe(this) { state ->
            when (state) {
                EstadoTrivia.CARGANDO -> showLoading(true)
                EstadoTrivia.MOSTRANDO -> {showLoading(false)}
                EstadoTrivia.FINALIZADO -> showGameFinished()
            }
        }

        triviaViewModel.loadNextQuestion()
    }

}