package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.repository.EstadisticasRepository
import com.example.greenquest.repository.LogrosRepository
import com.example.greenquest.repository.ScannerRepository
import com.example.greenquest.repository.TiendaAdquiridosRepository
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.states.ScanState
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EscanearModel: ViewModel() {
    private val qrScanner: BarcodeScanner by lazy {
        BarcodeScanning.getClient()
    }
    private val _scanState = MutableLiveData<ScanState>(ScanState.Idle)
    val scanState: LiveData<ScanState> = _scanState


    fun processImage(image: InputImage, onFinished: ()-> Unit) {

        viewModelScope.launch {
            try {

                val barcodes = qrScanner.process(image).await()
                if (barcodes.isEmpty()) return@launch
                var cantBarcodes = 0
                for (barcode in barcodes) {
                    cantBarcodes++
                    try {
                        // Procesamos el codigo de barras
                        val payload = ScannerRepository.processBarcode(barcode = barcode)
                        if (payload.idResiduo.isEmpty()) {
                            withContext(Dispatchers.Main){
                                _scanState.value = ScanState.HappyError("El QR ingresado no pertenece al de un contenedor inteligente")
                            }
                            return@launch
                        }
                        // Reclamamos el residuo
                        val response = ScannerRepository.reclamarResiduo(payload.idResiduo)

                        if (response.error.isNullOrEmpty()){


                            // Crear elemento del historial
                            EstadisticasRepository.insertarResiduoAlHistorial(payload)
                            TiendaAdquiridosRepository.addMonedas(payload.puntaje, UsuarioRepository.obtenerUsuarioLocal()!!.uid)
                            UsuarioRepository.incrementarCantidadResiduoLocal(payload.tipoResiduo)


                            withContext(Dispatchers.Main) {
                                _scanState.value = ScanState.QRDetected(payload)
                            }


                        } else {
                            withContext(Dispatchers.Main) {
                                _scanState.value = ScanState.HappyError(response.error)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("escanearLogging", "Hubo un error en process Image -> $e")
                    }
                }
            } catch (e: Exception) {
                Log.e("escanearLogging", "LA excepecion ocurre en linea 70 $e")
            } finally {
                onFinished()
            }
        }
    }
}

