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
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte
import com.example.greenquest.fragments.arguments.ReporteArgumentos
import com.example.greenquest.states.reporte.EstadoReporte
import com.example.greenquest.viewmodel.EstadisticaViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch


class HistorialResiduoCompletoFragment : Fragment() {
    private lateinit var estadisticaViewModel: EstadisticaViewModel
    private val origenHaciaReporte : OrigenHaciaReporte = OrigenHaciaReporte.HISTORIALCOMPLETO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        estadisticaViewModel = ViewModelProvider(this)[EstadisticaViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial_residuo_completo,
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estadisticaViewModel.obtenerResiduosEnEstado(null)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val recycler: RecyclerView = view.findViewById(
                    R.id.recycler_view_historial_reportes)
                val textoVacio: TextView = view.findViewById<TextView>(R.id.text_empty_state)


                estadisticaViewModel.residuos.collect { lista: List<HistorialResiduo> ->

                    if (lista.isEmpty()){
                        recycler.visibility = View.GONE
                        textoVacio.visibility = View.VISIBLE
                    } else {
                        recycler.visibility = View.VISIBLE
                        textoVacio.visibility = View.GONE
                        val adapterHistorialItem = AdapterHistorialItem(
                            lista,
                            onAlreadyReportedClick = {
                                    residuo ->
                                findNavController().navigate(
                                    HistorialResiduoCompletoFragmentDirections
                                        .actionHistorialResiduoCompletoFragmentToInformacionReporte(
                                            reporteArgumentos = ReporteArgumentos(
                                                idResiduo = residuo.idResiduo,
                                                origenHaciaReporte = origenHaciaReporte
                                            )
                                        )
                                )
                            },
                            onReportClick = {
                                    residuo ->
                                findNavController().navigate(
                                    HistorialResiduoCompletoFragmentDirections
                                        .actionHistorialResiduoCompletoFragmentToReportarFragment(
                                            reporteArgumentos = ReporteArgumentos(
                                                idResiduo = residuo.idResiduo,
                                                origenHaciaReporte = origenHaciaReporte
                                            )
                                        )
                                )
                            }
                        )
                        recycler.layoutManager = LinearLayoutManager(requireContext())
                        recycler.adapter = adapterHistorialItem
                    }

                }
            }
        }

        val botonVolver = view.findViewById<TextView>(R.id.link_volver_informe_reporte)
        botonVolver.setOnClickListener {

            findNavController()
                .navigate(
                    HistorialResiduoCompletoFragmentDirections
                        .actionHistorialResiduoCompletoFragmentToEstadisticasFragment())

        }

        val tabLayout: TabLayout = view.findViewById(R.id.tabs_filtro_residuos_reporte)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Mapeamos la posición de la pestaña al estado deseado
                val estado = when (tab?.position) {
                    0 -> null
                    1 -> EstadoReporte.REPORTADO
                    2 -> EstadoReporte.REPORTE_EXITOSO
                    3 -> EstadoReporte.REPORTE_FALLIDO
                    else -> null
                }
                estadisticaViewModel.obtenerResiduosEnEstado(estado)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }
}