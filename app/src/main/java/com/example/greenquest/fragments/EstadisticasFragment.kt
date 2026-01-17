package com.example.greenquest.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.example.greenquest.database.estadisticas.PeriodoResiduo
import com.example.greenquest.viewmodel.EstadisticaViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class EstadisticasFragment : Fragment() {

    private lateinit var estadisticaViewModel: EstadisticaViewModel

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    private var fechaInicioMillisPieChart: Long? = null
    private var fechaFinMillisPieChart: Long? = null

    private var fechaInicioMillisLineChart: Long? = null
    private var fechaFinMillisLineChart: Long? = null


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

        val tabsLineChart = view.findViewById<TabLayout>(R.id.tab_layout_periodos_line_chart)

        tabsLineChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position){
                    0 -> estadisticaViewModel.obtenerPuntosPorPeriodo(PeriodoResiduo.HOY)
                    1 -> estadisticaViewModel.obtenerPuntosPorPeriodo(PeriodoResiduo.SEMANA)
                    2 -> estadisticaViewModel.obtenerPuntosPorPeriodo(PeriodoResiduo.MES)
                    3 -> estadisticaViewModel.obtenerPuntosPorPeriodo(PeriodoResiduo.AÑO)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                Log.d("estadisticasLogging", "onTabUnselected")

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                Log.d("estadisticasLogging", "onTabReselected")
            }

        })
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
        barChart = view.findViewById(R.id.line_chart_historial_puntaje)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                estadisticaViewModel.residuosEntreFechas.collect { mapeo ->
                    showPieChart(mapeo)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                estadisticaViewModel.puntosEntreFechas.collect {
                    mapeo -> showBarChart(mapeo = mapeo)
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
            showDatePicker(
                fechaLimite = limiteSuperior,
                esSeleccionandoInicio = true,
                onDateSelected = { date, millis ->
                    fechaInicioInputPieChart.setText(date)
                    fechaInicioMillisPieChart = millis
                    estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, fechaFinMillisPieChart)
                },
                onDateCleared = {
                    fechaInicioInputPieChart.setText("") // Limpia el input
                    fechaInicioMillisPieChart = null    // Limpia la variable
                    estadisticaViewModel.obtenerResiduosEntreFechas(null, fechaFinMillisPieChart)
                }
            )
        }

        fechaFinInputPieChart.setOnClickListener {
            // Idea. Poner un limite inferior. Es obvio que no va a haber cosas antes de 2025,
            // pero por las dudas no lo pongo
            showDatePicker(
                fechaLimite = fechaInicioMillisPieChart,
                esSeleccionandoInicio = false,
                onDateSelected = { date, millis ->
                    fechaFinInputPieChart.setText(date)
                    fechaFinMillisPieChart = millis
                    estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, fechaFinMillisPieChart)
                },
                onDateCleared = {
                    fechaFinInputPieChart.setText("") // Limpia el input
                    fechaFinMillisPieChart = null    // Limpia la variable
                    estadisticaViewModel.obtenerResiduosEntreFechas(fechaInicioMillisPieChart, null)
                }
            )
        }
    }

    private fun showDatePicker(
        fechaLimite: Long?,
        esSeleccionandoInicio: Boolean,
        onDateSelected: (String, Long) -> Unit,
        onDateCleared: () -> Unit
    ) {
        val fechaHoy: Long = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder: CalendarConstraints = getCalendarConstraint(esSeleccionandoInicio, fechaLimite, fechaHoy)

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona una fecha")
            .setSelection(fechaLimite ?: MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder)
            .setNegativeButtonText("Limpiar")
            .setPositiveButtonText("Seleccionar")
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val instante = Instant.ofEpochMilli(selection).atZone(ZoneId.of("UTC")).toLocalDate()
            val formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val fechaFormateada = instante.format(formatterFecha)
            onDateSelected(fechaFormateada, selection)
        }

        picker.addOnNegativeButtonClickListener {
            onDateCleared()
        }

        picker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun getCalendarConstraint(
        esSeleccionandoInicio: Boolean,
        fechaLimite: Long?,
        fechaHoy: Long
    ): CalendarConstraints {
        val constraintsBuilder = CalendarConstraints.Builder()

        // La fecha limite en cualquier momento por defecto es hoy

        val validator = if (esSeleccionandoInicio) {
            // Si elijo INICIO: debe ser ANTES que la fecha de FIN
            val fin = fechaLimite ?: fechaHoy
            DateValidatorPointBackward.before(fin)
        } else {
            // Si elijo FIN: debe ser DESPUÉS que la fecha de INICIO y antes que HOY
            val inicio = fechaLimite ?: 0L // Si no hay inicio, cualquier fecha vieja sirve
            // Combinamos: después del inicio Y antes de hoy
            CompositeDateValidator.allOf(
                listOf(
                    DateValidatorPointForward.from(inicio),
                    DateValidatorPointBackward.now()
                )
            )
        }
        constraintsBuilder.setValidator(validator)
        return constraintsBuilder.build()
    }


    private fun showPieChart(mapeo: Map<TipoResiduo, Int>) {
        if (mapeo.isEmpty() || mapeo.keys.isEmpty()) {
            val textoAMostrar = if (fechaFinMillisPieChart == null && fechaInicioMillisPieChart == null) {
                "Usted no ha reciclado aún"
            } else {
                "No se recicló nada para el período seleccionado"
            }
            pieChart.clear()
            pieChart.setNoDataText(textoAMostrar)
            return
        }
        val pieEntries : ArrayList<PieEntry> = ArrayList()

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

        val pieDataSet = PieDataSet(pieEntries, "Tipos de residuos")
        pieDataSet.formSize = 10f // Color del cuadradito al lado del texto
        pieDataSet.valueTextSize = 25f // Tamaño del texto en el grafico
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieChart.description.isEnabled = false
        pieChart.extraBottomOffset = 10f // Ajusta márgenes externos
        pieChart.extraLeftOffset = 10f
        pieChart.extraRightOffset = 10f
        pieChart.setEntryLabelTextSize(25f) // Tamaño del label que esta en el grafico
        pieChart.setUsePercentValues(false) // Hace que no se usen valores porcentuales
        pieChart.isClickable = false
        pieChart.isScrollContainer = false
        pieChart.isRotationEnabled = true // Lo pondria en true porque es relajante girarlo

        pieChart.holeRadius = 0f
        pieChart.transparentCircleRadius = 0f
        pieChart.animateXY(1000,1000)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun showBarChart(mapeo: Map<String, Int>) {
        if (mapeo.isEmpty() || mapeo.keys.isEmpty()) {
            barChart.clear()
            val textoAMostrar = "Usted no ha reciclado aún"
            barChart.setNoDataText(textoAMostrar)
            barChart.setNoDataTextColor(R.color.texto_normal)
            return
        }
        barChart.setGridBackgroundColor(ContextCompat.getColor(requireContext(), R.color.acento_de_fondo))

        val sortedEntries = mapeo.entries.sortedBy {
            try {
                SimpleDateFormat("dd-mm", Locale.getDefault()).parse(it.key)
            } catch (_: Exception) {
                Date(0)
            }
        }

        val barEntries: ArrayList<BarEntry> = ArrayList()
        val labels: ArrayList<String> = ArrayList()

        sortedEntries.forEachIndexed { index, entry ->
            barEntries.add(BarEntry(index.toFloat(), entry.value.toFloat()))
            labels.add(formatDateToDayMonth(entry.key))
        }

        val barDataSet = BarDataSet(barEntries, "Puntos Obtenidos")

        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.accent_color)
        barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.accent_color)
        barDataSet.valueTextSize = 12f
        barDataSet.setDrawValues(true)


        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = labels.size
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                return if (index >= 0 && index < labels.size) {
                    labels[index]
                } else {
                    ""
                }
            }
        }
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.texto_normal)
        xAxis.textSize = 10f

        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.axisMinimum = 0f
        leftAxis.textColor = ContextCompat.getColor(requireContext(), R.color.texto_normal)
        leftAxis.textSize = 12f
        leftAxis.granularity = 1f

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        barChart.description.isEnabled = false

        val legend = barChart.legend
        legend.isEnabled = true
        legend.textColor = R.color.black
        legend.textSize = 12f

        // Configurar interacción
        barChart.setTouchEnabled(true)
        barChart.setDragEnabled(true)
        barChart.setScaleEnabled(true)
        barChart.setPinchZoom(false)

        barChart.animateY(1000)
        barChart.animateX(500)
        barChart.data = barData
        barChart.invalidate()

    }

    private fun formatDateToDayMonth(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (_: Exception) {
            Log.e("estadisticasLogging", "Hubo error en linea 395")
            return dateString
        }
    }



}