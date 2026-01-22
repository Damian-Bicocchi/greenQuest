package com.example.greenquest.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte
import com.example.greenquest.fragments.arguments.ReporteArgumentos


class EscaneadoExitoso : Fragment(R.layout.fragment_escaneado_exitoso) {

    private val args: EscaneadoExitosoArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datos = args.datosEscaneo

        "Reciclaste: ${datos.tipoResiduo}".also {
            view.findViewById<TextView>(R.id.label_resumen_residuo).text = it
        }

        "¡Felicidades! Sumaste ${datos.puntos} puntos".also {
            view.findViewById<TextView>(R.id.qr_mensaje_felicidades).text = it
        }

        view.findViewById<View>(R.id.button_qr_exitoso_continuar)
            .setOnClickListener {
                findNavController().navigate(
                    EscaneadoExitosoDirections
                        .actionEscaneadoExitosoFragmentToEscanearFragment()
                )
            }
        
        view.findViewById<Button>(R.id.button_denunciar_categoria)
            .setOnClickListener { 
                findNavController().navigate(
                    EscaneadoExitosoDirections.actionEscaneadoExitosoFragmentToReportarFragment(
                        reporteArgumentos = ReporteArgumentos(
                            origenHaciaReporte = OrigenHaciaReporte.ESCANEAR,
                            idResiduo = datos.idResiduo
                        )
                    )
                )
            }
    }


}
