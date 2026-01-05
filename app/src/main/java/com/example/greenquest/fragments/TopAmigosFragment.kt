package com.example.greenquest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greenquest.adapters.AdapterUsuarios
import com.example.greenquest.R
import com.example.greenquest.database.user.User

class TopAmigosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val toolbarContainer = activity!!.findViewById<View>(
            R.id.toolbar_container
        )
        toolbarContainer.visibility = View.VISIBLE
        val nombreFragmentactual = activity!!.findViewById<TextView>(R.id.nombreFragmentActualTextView)
        nombreFragmentactual.text = "Top Amigos"
        return inflater.inflate(R.layout.fragment_top_amigos, container, false)
    }

    override fun onResume() {
        super.onResume()
        val toolbarContainer = activity!!.findViewById<View>(
            R.id.toolbar_container
        )
        toolbarContainer.visibility = View.VISIBLE
    }
}