package com.example.greenquest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterLogro(val listaLogros: List<Logro>) : RecyclerView.Adapter<AdapterLogro.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val nombreLogro = view.findViewById<TextView>(R.id.nombreLogro)
        val descripcionLogro = view.findViewById<TextView>(R.id.descripcionLogro)
        val imagenLogro = view.findViewById<ImageView>(R.id.imagenLogro)

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


    }


    override fun getItemCount(): Int {
        return listaLogros.size
    }

}