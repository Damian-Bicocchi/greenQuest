package com.example.greenquest.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greenquest.R
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.viewmodel.TopGlobalViewModel

class TopGlobal : Fragment() {
    companion object {
        fun newInstance() = TopGlobal()
    }

    private var _binding: FragmentTopGlobalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TopGlobalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_top_global, container, false)
    }
}