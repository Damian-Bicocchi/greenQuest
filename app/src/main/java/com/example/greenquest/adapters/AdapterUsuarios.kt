package com.example.greenquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.database.user.User

class AdapterUsuarios(val usuarios: List<User>): RecyclerView.Adapter<AdapterUsuarios.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val rankingUsuarios: TextView = view.findViewById(R.id.rankingUsuario)
        val puntosUsuario: TextView = view.findViewById(R.id.puntosUsuario)

        val nombreUsuario: TextView = view.findViewById(R.id.nombre_usuario)

        val imagenUsuario: TextView = view.findViewById(R.id.placeholderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_top_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nombreUsuario.text = usuario.userName
        holder.rankingUsuarios.text= holder.rankingUsuarios.context.getString(R.string.ranking_position, position + 1)
        holder.puntosUsuario.text = holder.puntosUsuario.context.getString(R.string.x_points, usuario.puntos)
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