package com.example.greenquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.greenquest.R
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.database.tienda.Articulo

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

        holder.botonSeleccionar.setOnClickListener {
            val imageView = holder.itemView.rootView.findViewById<ImageView>(R.id.imagenActualCambiarImagen)
            val resId = item.imagen ?: R.drawable.outline_person_24
            imageView?.setImageResource(resId)
            imageView?.tag = resId
        }
    }

    override fun getItemCount(): Int {
        return articulos.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imagenArticulo: ImageView = view.findViewById<ImageView>(R.id.imagenSeleccionar)
        val botonSeleccionar: Button = view.findViewById<Button>(R.id.buttonSeleccionar)
    }



}