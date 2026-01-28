package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.Estacion
import com.example.greenquest.repository.EstacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel()  {
    private val _stations = MutableStateFlow<List<Estacion>>(emptyList())
    val stations: StateFlow<List<Estacion>> = _stations.asStateFlow()

    fun obtenerEstaciones() = viewModelScope.launch {
        _stations.value = EstacionRepository.listStations()
    }
}