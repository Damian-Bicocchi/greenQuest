package com.example.greenquest.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.apiParameters.RankingEntry

class GlobalRankingAdapter(private var dataset: List<RankingEntry>) : RecyclerView.Adapter<GlobalRankingAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        holder.placeholderView.text = dataset[position].username.subSequence(0,1)
        holder.usernameView.text = dataset[position].username
        holder.rankingView.text = "Ranking #${position + 1}"
        holder.scoreView.text = "${dataset[position].totalPuntos}\npuntos"
    }

    override fun getItemCount() = dataset.size
}