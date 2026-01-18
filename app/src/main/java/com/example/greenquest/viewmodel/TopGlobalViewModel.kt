package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.RankingEntry
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopGlobalViewModel : ViewModel() {
    private val _ranking = MutableStateFlow<List<RankingEntry>>(emptyList())
    val ranking: StateFlow<List<RankingEntry>> = _ranking.asStateFlow()
    var tipoResiduo: TipoResiduo? = null
    var historical: Boolean = false

    fun obtenerRanking() {
        viewModelScope.launch {
            // Si es histórico, podemos encontrar la posición del usuario.
            _ranking.value = if (historical) {
                val user = UsuarioRepository.obtenerUsuarioLocal()
                val selfRank = UsuarioRepository.rankingPosition()
                val selfScore = UsuarioRepository.score()
                val fetched = UsuarioRepository.rankingHistorical(tipoResiduo)
                if (selfRank <= 10) { // O ya está en el top o no está en absoluto.
                    fetched
                } else {
                    val newRanking = ArrayList<RankingEntry>()
                    newRanking.addAll(fetched)
                    newRanking.add(
                        RankingEntry(
                            user?.userName ?: "Vos",
                            selfScore
                        )
                    )
                    newRanking
                }
            } else {
                UsuarioRepository.rankingWeekly(tipoResiduo)
            }
        }
    }
}