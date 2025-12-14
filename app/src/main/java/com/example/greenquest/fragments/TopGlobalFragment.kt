package com.example.greenquest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.R
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.viewmodel.TopGlobalModel

class TopGlobalFragment : Fragment() {
    private var _binding: FragmentTopGlobalBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TopGlobalModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TopGlobalModel::class]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = binding.root

        return inflater.inflate(R.layout.fragment_top_global, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopGlobalFragment().apply {}
    }
}