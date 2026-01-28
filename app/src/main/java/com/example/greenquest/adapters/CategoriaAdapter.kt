package com.example.greenquest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.greenquest.R
import com.example.greenquest.enums.Categoria

class CategoriaAdapter(context: Context,
                       @param:LayoutRes @field:LayoutRes private val layoutRes: Int = R.layout.dropdown_item
) : ArrayAdapter<Categoria>(context, layoutRes, Categoria.entries.toTypedArray())
{
    private class ViewHolder(val textView: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            holder = ViewHolder(view.findViewById(android.R.id.text1))
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position) ?: return view
        holder.textView.text = item.getString(context)
        return view
    }
}