package com.example.greenquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.logros.Logro
import com.example.greenquest.R

class AdapterLogro(val listaLogros: List<Logro>) : RecyclerView.Adapter<AdapterLogro.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val nombreLogro: TextView = view.findViewById(R.id.nombreLogro)
        val descripcionLogro: TextView = view.findViewById(R.id.descripcionLogro)
        val imagenLogro: ImageView = view.findViewById(R.id.imagenLogro)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(R.layout.fila_logro, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = listaLogros[position]
        holder.nombreLogro.text = item.nombre
        holder.descripcionLogro.text = item.descripcion
        holder.imagenLogro.setImageResource(item.imagen)
        if(!item.obtenido){
            holder.imagenLogro.alpha = 0.3f
            holder.nombreLogro.alpha = 0.3f
            holder.descripcionLogro.alpha = 0.3f
        } else {
            holder.imagenLogro.alpha = 1.0f
            holder.nombreLogro.alpha = 1.0f
            holder.descripcionLogro.alpha = 1.0f
        }


    }


    override fun getItemCount(): Int {
        return listaLogros.size
    }

}