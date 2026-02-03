package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import com.example.greenquest.database.escaneo.DatosEscaneo
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte
import com.example.greenquest.fragments.arguments.ReporteArgumentos


private const val ARG_DATOS = "datos_escaneo"


class EscaneadoExitoso : Fragment() {

    private val args: EscaneadoExitosoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("greenQuestFragment", "HEre it is")
        return inflater.inflate(R.layout.fragment_escaneado_exitoso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datos = args.datosEscaneo

        Log.d("escanearLogging", "datos es ${datos.idResiduo} - ${datos.puntos} - ${datos.tipoResiduo}")
        datos.let {
            view.findViewById<TextView>(
                R.id.label_resumen_residuo).text = getString(R.string.you_recycled, it.tipoResiduo)
            view.findViewById<TextView>(R.id.qr_mensaje_felicidades).text =
                buildString {
                    append(getString(R.string.congratulations_you_gained))
                    append(" ")
                    append(it.puntos)
                    append(" ")
                    append(getString(R.string.points))
                }
        }

        val buttonDenunciar = view.findViewById<View>(R.id.button_denunciar_categoria)
        val buttonContinuar = view.findViewById<View>(R.id.button_qr_exitoso_continuar)

        buttonContinuar.setOnClickListener {
            val action = EscaneadoExitosoDirections.actionEscaneadoExitosoFragmentToEscanearFragment()
            findNavController().navigate(action)
        }

        buttonDenunciar.setOnClickListener {
            val reporteArgumentos = ReporteArgumentos(
                origenHaciaReporte = OrigenHaciaReporte.ESCANEAR,
                idResiduo = datos.idResiduo
            )
            val action = EscaneadoExitosoDirections.actionEscaneadoExitosoFragmentToReportarFragment(reporteArgumentos = reporteArgumentos)
            findNavController().navigate(action)

        }

    }


}