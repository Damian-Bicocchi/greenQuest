package com.example.greenquest.enums

import android.content.Context
import androidx.annotation.StringRes
import com.example.greenquest.R
import com.example.greenquest.apiParameters.TipoResiduo

enum class Categoria(@param:StringRes @field:StringRes private val stringId: Int, val tipoResiduo: TipoResiduo?) {
    TODOS(R.string.categoryTodos, null),
    CARTON(R.string.categoryCarton, TipoResiduo.CARTON),
    PLASTICO(R.string.categoryPlastico, TipoResiduo.PLASTICO),
    VIDRIO(R.string.categoryVidrio, TipoResiduo.VIDRIO),
    METAL(R.string.categoryMetal, TipoResiduo.METAL),
    PAPEL(R.string.categoryPapel, TipoResiduo.PAPEL);

    fun getString(context: Context): String = context.getString(stringId)

    override fun toString(): String {
        return super.toString()
    }
}