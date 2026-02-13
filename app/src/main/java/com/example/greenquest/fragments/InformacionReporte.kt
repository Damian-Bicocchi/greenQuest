package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenquest.R
import com.example.greenquest.fragments.arguments.OrigenHaciaReporte
import com.example.greenquest.viewmodel.ReporteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import kotlin.getValue


class InformacionReporte : Fragment() {

    private val args: InformacionReporteArgs by navArgs()
    private lateinit var reporteViewModel: ReporteViewModel
    private lateinit var linkVolver : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informacion_reporte, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reporteViewModel = ViewModelProvider(this)[ReporteViewModel::class.java]
        linkVolver = view.findViewById<TextView>(R.id.link_volver_informe_reporte)

        linkVolver.setOnClickListener {
            volverAlOrigenUI()
        }

        val textoCategoria = view.findViewById<TextView>(R.id.text_clasificacion_dada)
        lifecycleScope.launch {
            textoCategoria.text = reporteViewModel
                .obtenerCategoriaDenunciada(args.reporteArgumentos.idResiduo)
                .toString()
        }

        val imagenReporte = view.findViewById<ImageView>(R.id.image_thumbnail_reportar_informe)
        lifecycleScope.launch {
            imagenReporte.setImageDrawable(
                reporteViewModel
                    .obtenerImagenReporte(
                        requireContext(),
                        idResiduo = args.reporteArgumentos.idResiduo
                    )
            )
        }



        val botonCancelar = view.findViewById<Button>(R.id.btn_cancelar_reporte)

        botonCancelar.setOnClickListener {
            mostrarDialogConfirmacion(args.reporteArgumentos.idResiduo)
        }

    }

    private fun volverAlOrigenUI() {
        when (args.reporteArgumentos.origenHaciaReporte) {
            OrigenHaciaReporte.ESCANEAR -> {
                findNavController().navigate(
                    InformacionReporteDirections
                        .actionInformacionReporteToHistorialResiduoCompletoFragment())
            }

            OrigenHaciaReporte.ESTADISTICA -> {
                findNavController().navigate(InformacionReporteDirections
                    .actionInformacionReporteToEstadisticasFragment())
            }

            OrigenHaciaReporte.HISTORIALCOMPLETO -> {
                findNavController().navigate(InformacionReporteDirections
                    .actionInformacionReporteToHistorialResiduoCompletoFragment())
            }
        }
    }

    private fun mostrarDialogConfirmacion(idResiduo: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar reporte")
            .setMessage("¿Estás seguro de que deseas eliminar este reporte? Esta acción no se puede deshacer.")
            .setNeutralButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Eliminar reporte") { _, _ ->
                eliminarReporteConfirmado(idResiduo)
            }
            .show()
    }

    private fun eliminarReporteConfirmado(idResiduo: String) {
        reporteViewModel.eliminarReporte(idResiduo = idResiduo)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¡Muchas gracias!")
            .setMessage("El reporte ha sido eliminado correctamente.")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                volverAlOrigenUI()
            }
            .show()

    }


}