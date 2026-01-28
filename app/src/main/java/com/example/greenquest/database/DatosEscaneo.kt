package com.example.greenquest.database

import android.os.Parcelable
import com.example.greenquest.apiParameters.TipoResiduo
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DatosEscaneo(
    val tipoResiduo: TipoResiduo,
    val puntos: Int,
) : Parcelable