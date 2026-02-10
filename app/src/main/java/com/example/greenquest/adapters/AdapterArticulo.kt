package com.example.greenquest.adapters


import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.R
import com.example.greenquest.databinding.FilaTiendaBinding
import com.example.greenquest.viewmodel.TiendaViewModel
import com.example.greenquest.database.tienda.ArticuloTiendaUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterArticulo(
    val listaArticulo: MutableList<ArticuloTiendaUI>,
    private val onCompraExitosa: () -> Unit
) : RecyclerView.Adapter<AdapterArticulo.ViewHolder>() {

    private var adapterScope = CoroutineScope(Dispatchers.Main)

    private val tiendaViewModel = TiendaViewModel()


    class ViewHolder(val binding: FilaTiendaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = FilaTiendaBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listaArticulo[position]

        holder.binding.imagenArticulo.setImageResource(item.articulo.imagen ?: R.drawable.podium)
        holder.binding.nombreArticuloTienda.text = item.articulo.nombre

        holder.binding.nombreArticuloTienda.alpha = 1f
        holder.binding.imagenArticulo.alpha = 1f
        holder.binding.textviewPrecioArticulo.alpha = 1f

        if (item.adquirido) {
            holder.binding.nombreArticuloTienda.alpha = 0.5f
            holder.binding.imagenArticulo.alpha = 0.5f
            holder.binding.textviewPrecioArticulo.alpha = 0.5f
            holder.binding.textviewPrecioArticulo.text = "Adquirido"

            holder.binding.textviewPrecioArticulo.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Ya has adquirido este artículo", Toast.LENGTH_SHORT).show()
            }
        } else {
            holder.binding.textviewPrecioArticulo.text = "${item.articulo.valor}"

            holder.binding.textviewPrecioArticulo.setOnClickListener {

                val pos = holder.bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

                adapterScope.launch {
                    if (tiendaViewModel.comprarArticulo(listaArticulo[pos].articulo)) {

                        notifyItemChanged(pos)

                        Toast.makeText(
                            holder.itemView.context,
                            "Artículo comprado",
                            Toast.LENGTH_SHORT
                        ).show()

                        onCompraExitosa()

                    } else {
                        Toast.makeText(
                            holder.itemView.context,
                            "No tenés monedas suficientes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listaArticulo.size
    }




}