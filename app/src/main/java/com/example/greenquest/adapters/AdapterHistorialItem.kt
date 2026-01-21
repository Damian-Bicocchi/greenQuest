package com.example.greenquest.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.text.capitalize
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Locale.getDefault

class AdapterHistorialItem(
    val listaResiduos: List<HistorialResiduo>,
    private val onReportClick: (HistorialResiduo) -> Unit // Callback
) : RecyclerView.Adapter<AdapterHistorialItem.viewHolder>() {
    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val iconoLogoResiduo = view.findViewById<ImageView>(R.id.icono_logo_residuo)
        val textoNombreResiduo = view.findViewById<TextView>(R.id.texto_nombre_residuo)
        val textoFechaResiduo = view.findViewById<TextView>(R.id.texto_fecha_residuo)
        val textoPuntaje = view.findViewById<TextView>(R.id.texto_puntos_extra)

        val botonReportar = view.findViewById<Button>(R.id.button_denunciar_categoria)
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
        holder.iconoLogoResiduo.setImageDrawable(getLogoParaTipoResiduo(residuoParticular.tipoResiduo))
        val fechaFormateada = residuoParticular.fecha?.let { fecha ->
            val dia = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val hora = fecha.format(DateTimeFormatter.ofPattern("HH:mm"))
            "$dia • $hora"
        } ?: "Fecha desconocida"
        holder.textoFechaResiduo.text = fechaFormateada

        holder.textoNombreResiduo.text = residuoParticular.tipoResiduo.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
        holder.textoPuntaje.text = "+" + residuoParticular.puntosDados
        holder.botonReportar.setOnClickListener {
            onReportClick(residuoParticular)
        }

    }

    private fun getLogoParaTipoResiduo(tipoResiduo: TipoResiduo): Drawable? {
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