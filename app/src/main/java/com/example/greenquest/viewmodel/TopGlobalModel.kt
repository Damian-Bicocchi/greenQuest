package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.User
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class TopGlobalModel() : ViewModel() {
    private val _ranking = MutableStateFlow<List<User>>(emptyList())
    val ranking: StateFlow<List<User>> = _ranking.asStateFlow()

    fun obtenerRanking(tipoResiduo: TipoResiduo? = null) {
        viewModelScope.launch {
            try {
                val ranking = UsuarioRepository.ranking(tipoResiduo)
                val user = UsuarioRepository.obtenerUsuarioLocal() ?: return@launch
                val selfRank = RetrofitInstance.api.rankingPosition(user.uid)
                if (selfRank.posicion <= 10) {
                    _ranking.value = ranking
                } else {
                    val newRanking = ArrayList<User>()
                    newRanking.addAll(ranking)
                    newRanking.add(
                        User(
                            Int.MIN_VALUE,
                            user.userName ?: "ERROR",
                            puntos = user.puntos,
                            password = null,
                            accessToken = null,
                            refreshToken = null
                        )
                    )
                    _ranking.value = newRanking
                }
            } catch (e: IOException) {
                Log.e(TopGlobalModel::class.toString(), "FIXME: Error de IO al obtener ranking")
            } catch (e: HttpException) {
                Log.e(TopGlobalModel::class.toString(), "FIXME: Error de conexión al obtener ranking")
            }
        }
    }
}