package com.example.greenquest.viewmodel

import com.example.greenquest.database.escaneo.QrPayloadResiduo

sealed class ScanState {
    object Idle : ScanState()
    data class QRDetected(val payload: QrPayloadResiduo) : ScanState()
    data class Error(val message: String) : ScanState()

    data class HappyError(val message: String) : ScanState()

    data class QrException(val message: String) : ScanState()

}