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

    fun obtenerRanking() {
        viewModelScope.launch {
            val ranking = UsuarioRepository.rankingWeekly(tipoResiduo)
            _ranking.value = ranking

            // Bloque de código por si funcionara como esperamos la api para obtener la posición
            // en el ranking. Ahora mismo este no permite preguntar por la posición en el ranking
            // semanal.
            /*val user = UsuarioRepository.obtenerUsuarioLocal()
            val selfRank = UsuarioRepository.rankingPosition()
            val selfScore = UsuarioRepository.score()
            if (selfRank <= 10) {
                _ranking.value = ranking
            } else {
                val newRanking = ArrayList<RankingEntry>()
                newRanking.addAll(ranking)
                newRanking.add(
                    RankingEntry(
                        user?.userName?: "Error",
                        selfScore
                    )
                )
                _ranking.value = newRanking
            }*/
        }
    }
}