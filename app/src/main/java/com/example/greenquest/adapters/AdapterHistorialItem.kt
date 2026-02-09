package com.example.greenquest.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.states.reporte.EstadoReporte
import java.time.format.DateTimeFormatter
import java.util.Locale.getDefault

class AdapterHistorialItem(
    val listaResiduos: List<HistorialResiduo>,
    private val onReportClick: (HistorialResiduo) -> Unit,
    private val onAlreadyReportedClick: (HistorialResiduo) -> Unit
) : RecyclerView.Adapter<AdapterHistorialItem.viewHolder>() {
    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val iconoLogoResiduo: ImageView? = view.findViewById<ImageView>(R.id.icono_imagen_reporte)
        val textoNombreResiduo: TextView? = view.findViewById<TextView>(R.id.texto_reportado_como)
        val textoFechaResiduo: TextView? = view.findViewById<TextView>(R.id.texto_fecha_reporte)
        val textoPuntaje: TextView? = view.findViewById<TextView>(R.id.texto_puntos_extra)

        val botonReportar: Button? = view.findViewById<Button>(R.id.button_ver_reporte)
    }

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_residuo_historial, parent, false)
        this.parent = parent
        return viewHolder(view = view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val residuoParticular = listaResiduos[position]
        holder.iconoLogoResiduo?.setImageDrawable(getLogoParaTipoResiduo(residuoParticular.tipoResiduo, holder))
        val fechaFormateada = residuoParticular.fecha?.let { fecha ->
            val dia = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val hora = fecha.format(DateTimeFormatter.ofPattern("HH:mm"))
            "$dia • $hora"
        } ?: "Fecha desconocida"
        holder.textoFechaResiduo?.text = fechaFormateada

        holder.textoNombreResiduo?.text = residuoParticular.tipoResiduo.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
        holder.textoPuntaje?.text = buildString {
            append("+")
            append(residuoParticular.puntosDados)
        }



        when (residuoParticular.estadoReporte) {
            EstadoReporte.SIN_REPORTE -> {
                holder.botonReportar?.text = holder.botonReportar.context.getString(R.string.report)
                holder.botonReportar?.setBackgroundColor(
                    ContextCompat.getColor(holder.view.context, R.color.texto_rojo))
                holder.botonReportar?.setOnClickListener {
                    onReportClick(residuoParticular)
                }
            }
            EstadoReporte.REPORTADO -> {
                holder.botonReportar?.text = holder.botonReportar.context.getString(R.string.see_report)

                holder.botonReportar?.setOnClickListener {
                    onAlreadyReportedClick(residuoParticular)
                }
            }
            EstadoReporte.REPORTE_FALLIDO -> {
                Log.d("greenQuest", "No implementado")
            }

            EstadoReporte.REPORTE_EXITOSO -> {

                Log.d("greenQuest", "No implementado")
            }
        }


    }

    private fun getLogoParaTipoResiduo(tipoResiduo: TipoResiduo, holder: viewHolder): Drawable? {
        holder.iconoLogoResiduo?.contentDescription = "Logo de " + tipoResiduo.name
        val drawableId = when(tipoResiduo){
            TipoResiduo.CARTON -> R.drawable.ic_carton
            TipoResiduo.PLASTICO -> R.drawable.ic_plastico
            TipoResiduo.VIDRIO -> R.drawable.ic_vidrio
            TipoResiduo.METAL -> R.drawable.ic_metal
            TipoResiduo.PAPEL -> R.drawable.ic_papel
            TipoResiduo.BASURA -> R.drawable.ic_basura

        }
        return drawableId.let {
            ContextCompat.getDrawable(parent.context, it)
        } ?: ContextCompat.getDrawable(parent.context, R.drawable.ic_basura)
    }

    override fun getItemCount(): Int {
        return listaResiduos.count()
    }
}