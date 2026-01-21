package com.example.greenquest.adapters


import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.Articulo
import com.example.greenquest.R
import com.example.greenquest.databinding.FilaTiendaBinding

class AdapterArticulo(val listaArticulo: List<Articulo>) : RecyclerView.Adapter<AdapterArticulo.ViewHolder>() {

    lateinit var binding : FilaTiendaBinding
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val nombreArticulo = view.findViewById<TextView>(binding.nombreArticuloTienda.id)
        val imagenArticulo = view.findViewById<ImageView>(binding.imagenArticulo.id)
        val valorArticulo = view.findViewById<TextView>(binding.textviewPrecioArticulo.id)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = FilaTiendaBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item  = listaArticulo[position]


        holder.imagenArticulo.setImageResource(item.imagen?: R.drawable.podium)
        holder.nombreArticulo.text = item.nombre
        if(item.adquirido){
            holder.nombreArticulo.alpha = 0.5f
            holder.imagenArticulo.alpha = 0.5f
            holder.valorArticulo.alpha = 0.5f
            holder.valorArticulo.text = "Adquirido"
        }else {
            holder.valorArticulo.text = item.valor
        }
    }

    override fun getItemCount(): Int {
        return listaArticulo.size
    }




}