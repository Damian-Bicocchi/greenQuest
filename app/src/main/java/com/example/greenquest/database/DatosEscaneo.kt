package com.example.greenquest.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatosEscaneo(
    val tipoResiduo: com.example.greenquest.apiParameters.TipoResiduo,
    val puntos: Int,
) : Parcelable