package com.example.greenquest.states.reporte

// Si, no es para nada bonito lo que estoy haciendo
// mas funciona
sealed class EstadoReporteUI {
    object SinReporte : EstadoReporteUI()
    object Reportado : EstadoReporteUI()
    data class ReporteFallido(val mensaje: String) : EstadoReporteUI()
}