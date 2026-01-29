package com.example.greenquest.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import java.time.format.DateTimeFormatter
import java.util.Locale.getDefault

class AdapterHistorialItem(val listaResiduos: List<HistorialResiduo>) : RecyclerView.Adapter<AdapterHistorialItem.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val iconoLogoResiduo: ImageView = view.findViewById(R.id.icono_logo_residuo)
        val textoNombreResiduo: TextView = view.findViewById(R.id.texto_nombre_residuo)
        val textoFechaResiduo: TextView = view.findViewById(R.id.texto_fecha_residuo)
        val textoHoraResiduo: TextView = view.findViewById(R.id.texto_hora_residuo)
        val textoPuntaje: TextView = view.findViewById(R.id.texto_puntos_extra)
    }

    private lateinit var parent: ViewGroup

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_residuo_historial, parent, false)
        this.parent = parent
        return ViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val residuoParticular = listaResiduos[position]
        holder.iconoLogoResiduo.setImageDrawable(getLogoParaTipoResiduo(residuoParticular.tipoResiduo))
        val formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        holder.textoFechaResiduo.text = residuoParticular.fecha?.format(formatterFecha)
        val formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss")
        holder.textoHoraResiduo.text = residuoParticular.fecha?.format(formatterHora)
        holder.textoNombreResiduo.text = residuoParticular.tipoResiduo.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() }
        holder.textoPuntaje.text = "+" + residuoParticular.puntosDados


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
        return drawableId?.let {
            ContextCompat.getDrawable(parent.context, it)
        } ?: ContextCompat.getDrawable(parent.context, R.drawable.ic_basura)
    }

    override fun getItemCount(): Int {
        return listaResiduos.count()
    }
}