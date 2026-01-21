package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte

class ReportarFragment : Fragment() {

    private val args: ReportarFragmentArgs by navArgs()

    private lateinit var origen: OrigenHaciaReporte
    private lateinit var idResiduo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        origen = args.reporteArgumentos.origenHaciaReporte
        idResiduo = args.reporteArgumentos.idResiduo

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reportar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonEnviarReporte = view.findViewById<Button>(R.id.button_enviar_reporte)

        buttonEnviarReporte.setOnClickListener {
            Log.e("reporteLogging", "Vengo desde $origen")
        }

        val volver = view.findViewById<TextView>(R.id.link_volver_reporte)
        volver.setOnClickListener {
            when(origen){
                OrigenHaciaReporte.ESCANEAR -> {
                    findNavController().navigate(ReportarFragmentDirections.actionReportarFragmentToEscanearFragment())
                }
                OrigenHaciaReporte.ESTADISTICA -> {
                    findNavController().navigate(ReportarFragmentDirections.actionReportarFragmentToEstadisticasFragment())
                }
                OrigenHaciaReporte.HISTORIALCOMPLETO -> {
                    findNavController().navigate(ReportarFragmentDirections.actionReportarFragmentToHistorialResiduoCompletoFragment())
                }
            }
        }

    }

}