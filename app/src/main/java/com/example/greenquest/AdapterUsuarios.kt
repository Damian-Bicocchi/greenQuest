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
        if (position+1 == 1) {
            val oro = holder.view.context.getColor(R.color.fila_usuario_oro)
            holder.view.setBackgroundColor(oro)
        } else if (position+1 == 2) {
            val plata = holder.view.context.getColor(R.color.fila_usuario_plata)
            holder.view.setBackgroundColor(plata)
        } else if (position+1 == 3) {
            val bronce = holder.view.context.getColor(R.color.fila_usuario_bronce)
            holder.view.setBackgroundColor(bronce)
        }
        //cargar imagen si es necesario
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }


}