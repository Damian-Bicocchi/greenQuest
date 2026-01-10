package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.greenquest.R
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.viewmodel.EstadisticaViewModel
import kotlinx.coroutines.launch


class EstadisticasFragment : Fragment() {


    private lateinit var estadisticaViewModel: EstadisticaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        estadisticaViewModel = ViewModelProvider(this)[EstadisticaViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadisticas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estadisticaViewModel.obtenerResiduos()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                estadisticaViewModel.residuos.collect { lista: List<HistorialResiduo> ->
                    if (lista.isNotEmpty()) {
                        Log.e("estadisticasLogging", "Lista de residuos: $lista")
                    } else {
                        Log.e("estadisticasLogging", "Esta vacia la lista")
                    }
                }
            }
        }
    }


}