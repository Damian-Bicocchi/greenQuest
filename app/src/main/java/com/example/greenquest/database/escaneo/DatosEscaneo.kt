package com.example.greenquest.database.escaneo

import android.os.Parcelable
import com.example.greenquest.apiParameters.TipoResiduo
import kotlinx.parcelize.Parcelize

@Parcelize
data class DatosEscaneo(
    val tipoResiduo: TipoResiduo,
    val puntos: Int,
) : Parcelable