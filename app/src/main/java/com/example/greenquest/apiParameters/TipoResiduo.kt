package com.example.greenquest.apiParameters

import android.content.Context
import androidx.annotation.ColorRes
import com.example.greenquest.R
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale.getDefault

@Serializable
enum class TipoResiduo(@param:ColorRes @field:ColorRes val colorRes: Int) {

    @SerialName("Carton")
    CARTON(R.color.color_carton),

    @SerialName("Plastico")
    PLASTICO(R.color.color_plastico),

    @SerialName("Vidrio")
    VIDRIO(R.color.color_vidrio),

    @SerialName("Metal")
    METAL(R.color.color_metal),

    @SerialName("Papel")
    PAPEL(R.color.color_papel),

    @SerialName("Basura")
    BASURA(R.color.color_basura);

    @OptIn(ExperimentalSerializationApi::class)
    override fun toString() =
        serializer().descriptor.getElementName(ordinal)
            .lowercase()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString()
            }

    fun getString(context: Context): String = context.getString(when (this) {
        TipoResiduo.CARTON -> R.string.categoryCarton
        TipoResiduo.PLASTICO -> R.string.categoryPlastico 
        TipoResiduo.VIDRIO -> R.string.categoryVidrio
        TipoResiduo.METAL -> R.string.categoryMetal
        TipoResiduo.PAPEL -> R.string.categoryPapel
        TipoResiduo.BASURA -> R.string.categoryBasura
    })
}

