package com.example.greenquest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterUsuarios(val usuarios: List<User>): RecyclerView.Adapter<AdapterUsuarios.viewHolder>() {

    class viewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val rankingUsuarios = view.findViewById<TextView>(R.id.rankingUsuario)
        val puntosUsuario = view.findViewById<TextView>(R.id.puntosUsuario)

        val nombreUsuario = view.findViewById<TextView>(R.id.nombre_usuario)

        val imagenUsuario = view.findViewById<TextView>(R.id.placeholderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_top_usuario, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nombreUsuario.text = usuario.userName
        holder.rankingUsuarios.text= "Ranking #${position + 1}"
        holder.puntosUsuario.text = "${usuario.puntos}\npuntos"
        val card = holder.view as CardView

        val ctx = holder.view.context
        val color = when (position) {
            0 -> ctx.getColor(R.color.fila_usuario_oro)
            1 -> ctx.getColor(R.color.fila_usuario_plata)
            2 -> ctx.getColor(R.color.fila_usuario_bronce)
            else -> ctx.getColor(R.color.fila_usuario_normal)
        }

        card.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }


}