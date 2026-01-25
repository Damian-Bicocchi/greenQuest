package com.example.greenquest.adapters


import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.Articulo
import com.example.greenquest.R
import com.example.greenquest.databinding.FilaTiendaBinding
import com.example.greenquest.viewmodel.TiendaViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.datatransport.runtime.ExecutionModule_ExecutorFactory.executor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterArticulo(val listaArticulo: List<Articulo>) : RecyclerView.Adapter<AdapterArticulo.ViewHolder>() {

    lateinit var binding : FilaTiendaBinding

    private var adapterScope = CoroutineScope(Dispatchers.Main)

    private val tiendaViewModel = TiendaViewModel()

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
            binding.textviewPrecioArticulo.setOnClickListener {
               Toast.makeText(holder.itemView.context, "Ya has adquirido este artículo", Toast.LENGTH_SHORT).show()
            }
        }else {
            binding.textviewPrecioArticulo.setOnClickListener {
                adapterScope.launch {
                    if (tiendaViewModel.comprarArticulo(item)) {
                        holder.nombreArticulo.alpha = 0.5f
                        holder.imagenArticulo.alpha = 0.5f
                        holder.valorArticulo.alpha = 0.5f
                        holder.valorArticulo.text = "Adquirido"
                        Toast.makeText(
                            holder.itemView.context,
                            "Has comprado ${item.nombre} por ${item.valor} monedas",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            holder.itemView.context,
                            "No tienes suficientes monedas para comprar ${item.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            holder.valorArticulo.text = "${item.valor} puntos"
        }
    }

    override fun getItemCount(): Int {
        return listaArticulo.size
    }




}