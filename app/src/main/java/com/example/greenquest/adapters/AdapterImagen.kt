package com.example.greenquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.greenquest.R
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.Articulo
import com.example.greenquest.databinding.FilaFotosPerfilBinding

class AdapterImagen (val articulos: List<Articulo>) : RecyclerView.Adapter<AdapterImagen.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(R.layout.fila_fotos_perfil, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = articulos[position]
        holder.imagenArticulo.setImageResource(item.imagen ?: R.drawable.outline_person_24)
        holder.botonSeleccionar.id = item.id
    }

    override fun getItemCount(): Int {
        return articulos.size
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imagenArticulo = view.findViewById<ImageView>(R.id.imagenSeleccionar)
        val botonSeleccionar = view.findViewById<ImageView>(R.id.buttonSeleccionar)
    }



}