package com.example.greenquest.viewmodel

import com.example.greenquest.QrPayloadResiduo

sealed class ScanState {
    object Idle : ScanState()
    data class QRDetected(val payload: QrPayloadResiduo) : ScanState()
    data class Error(val message: String) : ScanState()

}