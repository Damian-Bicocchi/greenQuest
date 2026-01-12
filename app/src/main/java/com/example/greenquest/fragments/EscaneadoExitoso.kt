package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import kotlin.getValue


class EscaneadoExitoso : Fragment(R.layout.fragment_escaneado_exitoso) {

    private val args: EscaneadoExitosoArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datos = args.datosEscaneo

        view.findViewById<TextView>(R.id.label_resumen_residuo).text =
            "Reciclaste: ${datos}"

        view.findViewById<TextView>(R.id.qr_mensaje_felicidades).text =
            "¡Felicidades! Sumaste ${datos.puntos} puntos"

        view.findViewById<View>(R.id.button_qr_exitoso_continuar)
            .setOnClickListener {
                findNavController().navigate(
                    EscaneadoExitosoDirections
                        .actionEscaneadoExitosoFragmentToEscanearFragment()
                )
            }
    }
}
