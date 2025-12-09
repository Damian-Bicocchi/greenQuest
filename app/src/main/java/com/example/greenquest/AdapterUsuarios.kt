package com.example.greenquest

import androidx.recyclerview.widget.RecyclerView

class AdapterUsuarios(val usuarios: List<Usuario>): RecyclerView.Adapter<AdapterUsuarios.viewHolder>() {

    class viewHolder(val view: android.view.View): RecyclerView.ViewHolder(view) {
        val rankingUsuarios = view.findViewById<android.widget.TextView>(R.id.rankingUsuario)
        val puntosUsuario = view.findViewById<android.widget.TextView>(R.id.puntosUsuario)
        val imagenUsuario = view.findViewById<android.widget.ImageView>(R.id.imagenUsuario)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): viewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_top_usuario, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.rankingUsuarios.text = "Ranking #${position + 1}"
        holder.puntosUsuario.text = "${usuario.puntos} " +
                "puntos"
        // cambiar color según posición
        val card = holder.view as androidx.cardview.widget.CardView

        val ctx = holder.view.context
        val color = when (position) {
            0 -> ctx.getColor(R.color.fila_usuario_oro)
            1 -> ctx.getColor(R.color.fila_usuario_plata)
            2 -> ctx.getColor(R.color.fila_usuario_bronce)
            else -> ctx.getColor(R.color.fila_usuario_normal)
        }

        card.setCardBackgroundColor(color)
        //cargar imagen si es necesario
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }


}