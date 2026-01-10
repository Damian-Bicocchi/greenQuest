package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.repository.LogrosRepository
import com.example.greenquest.repository.MonedasRepository
import com.example.greenquest.repository.ScannerRepository
import com.example.greenquest.states.ScanState
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EscanearModel: ViewModel() {
    private val qrScanner: BarcodeScanner by lazy {
        BarcodeScanning.getClient()
    }
    private val _scanState = MutableLiveData<ScanState>(ScanState.Idle)
    val scanState: LiveData<ScanState> = _scanState


    fun processImage(image: InputImage): Task<List<Barcode>> {
        return qrScanner.process(image)
            .addOnSuccessListener { barcodes ->
                viewModelScope.launch {
                    for (barcode in barcodes){
                        try {
                            // Procesamos el codigo de barras
                            val payload = ScannerRepository.processBarcode(barcode = barcode)

                            // Reclamamos el residuo
                            val response = ScannerRepository.reclamarResiduo(payload.id_residuo)

                            if (response.error.isNullOrEmpty()){
                                withContext(Dispatchers.Main) {
                                    _scanState.value = ScanState.QRDetected(payload)
                                }
                                // Agregarle monedas. Tantas monedas como puntajes da
                                MonedasRepository.addMonedas(payload.puntaje)

                                // Contabilizar para logros
                                LogrosRepository.incrementarContadorResiduo(payload.tipo_residuo)

                            } else {
                                withContext(Dispatchers.Main) {
                                    _scanState.value = ScanState.HappyError(response.error)
                                }
                            }
                        } catch (e : Exception){
                            withContext(Dispatchers.Main) {
                                Log.e("greenQuest", "La excepcion fue " + e.message)
                                _scanState.value = ScanState.QrException("Hubo un error inesperado")
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("greenQuest", "Error en el procesado de la imagen " + e.toString())
                _scanState.value = ScanState.QrException("ERROR INESPERADO")


            }

    }
}