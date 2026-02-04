package com.example.greenquest.fragments.arguments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReporteArgumentos(
    val origenHaciaReporte: OrigenHaciaReporte,
    val idResiduo: String
) : Parcelable
