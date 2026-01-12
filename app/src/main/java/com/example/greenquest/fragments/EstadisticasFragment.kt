package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterHistorialItem
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.viewmodel.EstadisticaViewModel
import kotlinx.coroutines.launch


class EstadisticasFragment : Fragment(R.layout.fragment_estadisticas) {

    private lateinit var estadisticaViewModel: EstadisticaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        estadisticaViewModel = ViewModelProvider(this)[EstadisticaViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estadisticaViewModel.obtenerResiduos()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                estadisticaViewModel.residuos.collect { lista ->
                    val listaFinal = if (lista.size > 3) lista.subList(0, 2) else lista
                    val adapter = AdapterHistorialItem(listaFinal)

                    val recycler = view.findViewById<RecyclerView>(R.id.recycler_view_historial)
                    recycler.layoutManager = LinearLayoutManager(requireContext())
                    recycler.adapter = adapter
                }
            }
        }

        val linkTodaActividad = view.findViewById<TextView>(R.id.link_toda_actividad)
        linkTodaActividad.setOnClickListener {
            findNavController().navigate(
                R.id.action_estadisticasFragment_to_historialResiduoCompletoFragment
            )
        }
    }
}
