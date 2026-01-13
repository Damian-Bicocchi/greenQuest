package com.example.greenquest.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import java.time.format.DateTimeFormatter

class AdapterHistorialItem(val listaResiduos: List<HistorialResiduo>) : RecyclerView.Adapter<AdapterHistorialItem.viewHolder>() {

    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val iconoLogoResiduo = view.findViewById<ImageView>(R.id.icono_logo_residuo)
        val textoNombreResiduo = view.findViewById<TextView>(R.id.texto_nombre_residuo)
        val textoFechaResiduo = view.findViewById<TextView>(R.id.texto_fecha_residuo)
        val textoHoraResiduo = view.findViewById<TextView>(R.id.texto_hora_residuo)
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
        val formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        holder.textoFechaResiduo.text = residuoParticular.fecha?.format(formatterFecha)
        holder.textoHoraResiduo.text = residuoParticular.fecha?.format(DateTimeFormatter.ISO_LOCAL_TIME)
        holder.textoNombreResiduo.text = residuoParticular.tipoResiduo.name
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