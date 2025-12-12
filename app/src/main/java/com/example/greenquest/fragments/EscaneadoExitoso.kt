package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.greenquest.R
import com.example.greenquest.database.DatosEscaneo


private const val ARG_DATOS = "datos_escaneo"


class EscaneadoExitoso : Fragment() {

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

        val datos = arguments?.getParcelable<DatosEscaneo>(ARG_DATOS)
        datos?.let {
            view.findViewById<TextView>(R.id.label_resumen_residuo).text = "Reciclaste: " + it.tipoResiduo
            view.findViewById<TextView>(R.id.qr_mensaje_felicidades).text = "¡Felicidades! Sumaste " + it.puntos + " puntos"
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(datosEscaneo: DatosEscaneo): EscaneadoExitoso {
            val fragment = EscaneadoExitoso()
            val args = Bundle()
            args.putParcelable(ARG_DATOS, datosEscaneo)
            fragment.arguments = args
            return fragment
        }
    }
}