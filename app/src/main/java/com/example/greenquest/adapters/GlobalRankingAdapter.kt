package com.example.greenquest.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.RankingEntry
import java.util.Locale.getDefault

class GlobalRankingAdapter(private var dataset: List<RankingEntry>) :
    RecyclerView.Adapter<GlobalRankingAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardItem)
        val placeholderView: TextView = view.findViewById(R.id.placeholderImage)
        val usernameView: TextView = view.findViewById(R.id.nombre_usuario)
        val rankingView: TextView = view.findViewById(R.id.rankingUsuario)
        val scoreView: TextView = view.findViewById(R.id.puntosUsuario)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newDataset: List<RankingEntry>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fila_top_usuario, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.setBackgroundResource(
            when (position) {
                0 -> R.color.fila_usuario_oro
                1 -> R.color.fila_usuario_plata
                2 -> R.color.fila_usuario_bronce
                else -> R.color.fila_usuario_normal
            }
        )
        holder.placeholderView.text = dataset[position].username.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
        }.subSequence(0, 1)
        holder.usernameView.text = dataset[position].username
        holder.rankingView.text = "Ranking #${position + 1}"
        holder.scoreView.text = "${dataset[position].total_puntos}\npuntos"
    }

    override fun getItemCount() = dataset.size
}