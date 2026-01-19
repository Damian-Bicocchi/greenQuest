package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterHistorialItem
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.viewmodel.EstadisticaViewModel
import kotlinx.coroutines.launch


class HistorialResiduoCompletoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_historial_residuo_completo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estadisticaViewModel.obtenerResiduos()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                estadisticaViewModel.residuos.collect { lista: List<HistorialResiduo> ->

                    val adapterHistorialItem = AdapterHistorialItem(lista)
                    val recycler: RecyclerView = view.findViewById(R.id.recycler_view_historial_completo)
                    recycler.layoutManager = LinearLayoutManager(requireContext())
                    recycler.adapter = adapterHistorialItem
                }
            }
        }

        val botonVolver = view.findViewById<TextView>(R.id.link_volver_a_estadisticas)
        botonVolver.setOnClickListener {

            findNavController().popBackStack()

        }


    }
}