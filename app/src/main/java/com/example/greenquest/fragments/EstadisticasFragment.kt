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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.adapters.AdapterHistorialItem
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.viewmodel.EstadisticaViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class EstadisticasFragment : Fragment() {

    private lateinit var estadisticaViewModel: EstadisticaViewModel

    private lateinit var pieChart: PieChart

    private var fechaInicioMillisPieChart: Long? = null
    private var fechaFinMillisPieChart: Long? = null


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

    override fun onPause() {
        super.onPause()
        fechaFinMillisPieChart = null
        fechaInicioMillisPieChart = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        estadisticaViewModel.obtenerResiduos()
        estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, fechaFinMillisPieChart)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                estadisticaViewModel.residuos.collect { lista: List<HistorialResiduo> ->
                    val listaFinal = if (lista.size > 3) lista.subList(0,2) else lista
                    val adapterHistorialItem = AdapterHistorialItem(listaFinal)
                    val recycler: RecyclerView = view.findViewById(R.id.recycler_view_historial)
                    recycler.layoutManager = LinearLayoutManager(requireContext())
                    recycler.adapter = adapterHistorialItem
                }
            }
        }
        pieChart = view.findViewById(R.id.pie_chart_tipo_residuo)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                estadisticaViewModel.residuosEntreFechas.collect { mapeo ->
                    // Aquí actualizas tu UI (gráficos, listas, etc.)
                    showPieChart(mapeo)
                }
            }
        }
        val linkTodaActividad = view.findViewById<TextView>(R.id.link_toda_actividad)

        linkTodaActividad.setOnClickListener {
            val fragment = HistorialResiduoCompletoFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit()

        }

        val fechaInicioInputPieChart = view
            .findViewById<TextInputEditText>(
                R.id.fecha_inicio_tipo_residuo_por_periodo)

        val fechaFinInputPieChart = view
            .findViewById<TextInputEditText>(
                R.id.fecha_fin_tipo_residuo_por_periodo)

        fechaInicioInputPieChart.setOnClickListener {
            val limiteSuperior = fechaFinMillisPieChart ?: MaterialDatePicker.todayInUtcMilliseconds()
            showDatePicker(fechaLimite = limiteSuperior, esSeleccionandoInicio = true) {
                date, millis ->
                fechaInicioInputPieChart.setText(date)
                fechaInicioMillisPieChart = millis
            }
            estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, fechaFinMillisPieChart)
        }

        fechaFinInputPieChart?.setOnClickListener {
            // Idea. Poner un limite inferior. Es obvio que no va a haber cosas antes de 2025,
            // pero por las dudas no lo pongo
            showDatePicker(fechaInicioMillisPieChart, false) {
                date, millis ->
                fechaFinInputPieChart.setText(date)
                fechaFinMillisPieChart = millis
            }
            estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, fechaFinMillisPieChart)

        }
    }

    private fun showDatePicker(
        fechaLimite: Long?,
        esSeleccionandoInicio: Boolean,
        onDateSelected: (String, Long) -> Unit
    ) {
        val constraintsBuilder = CalendarConstraints.Builder()

        // La fecha limite en cualquier momento por defecto es hoy
        val fechaHoy = MaterialDatePicker.todayInUtcMilliseconds()

        val validator = if (esSeleccionandoInicio) {
            // Si elijo INICIO: debe ser ANTES que la fecha de FIN
            val fin = fechaLimite ?: fechaHoy
            DateValidatorPointBackward.before(fin)
        } else {
            // Si elijo FIN: debe ser DESPUÉS que la fecha de INICIO y antes que HOY
            val inicio = fechaLimite ?: 0L // Si no hay inicio, cualquier fecha vieja sirve
            // Combinamos: después del inicio Y antes de hoy
            com.google.android.material.datepicker.CompositeDateValidator.allOf(
                listOf(
                    com.google.android.material.datepicker.DateValidatorPointForward.from(inicio),
                    DateValidatorPointBackward.now()
                )
            )
        }
        constraintsBuilder.setValidator(validator)

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona una fecha")
            .setSelection(fechaLimite ?: fechaHoy)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val instante = Instant.ofEpochMilli(selection).atZone(ZoneId.of("UTC")).toLocalDate()
            val formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val fechaFormateada = instante.format(formatterFecha)

            onDateSelected(fechaFormateada, selection)
        }

        picker.show(parentFragmentManager, "DATE_PICKER")
    }


    private fun showPieChart(mapeo: Map<TipoResiduo, Int>) {
        if (mapeo.isEmpty()) {
            val textoAMostrar = if (fechaFinMillisPieChart == null && fechaInicioMillisPieChart == null) {
                "Usted no ha reciclado aún"
            } else {
                "No se recicló nada para el período seleccionado"
            }
            pieChart.setNoDataText(textoAMostrar)
            return
        }
        var pieEntries : ArrayList<PieEntry> = ArrayList()

        val colorArray = resources.getIntArray(R.array.pieChartColorArray)
        val colors = colorArray.toList()

        for (elem in mapeo.keys){
            pieEntries.add(
                PieEntry(
                    mapeo[elem]?.toFloat() ?: 0f,
                    elem.toString()
                )
            )
        }
        val pieDataSet = PieDataSet(pieEntries, "texto a encontrar")
        pieDataSet.valueTextSize = 10f
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieChart.description.isEnabled = false
        pieChart.animateXY(100,100)
        pieChart.data = pieData
        pieChart.minimumHeight = 208
        pieChart.invalidate()

    }


}