package com.example.greenquest.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenquest.R
import com.example.greenquest.adapters.GlobalRankingAdapter
import com.example.greenquest.databinding.FragmentTopGlobalBinding
import com.example.greenquest.viewmodel.TopGlobalViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopGlobal : Fragment() {
    companion object {
        fun newInstance() = TopGlobal()
    }

    private var _binding: FragmentTopGlobalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TopGlobalViewModel by viewModels()
    private lateinit var adapter: GlobalRankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.obtenerRanking()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopGlobalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GlobalRankingAdapter(emptyList())
        binding.rankingList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TopGlobal.adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ranking.collectLatest { rankingEntries ->
                adapter.update(rankingEntries)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}