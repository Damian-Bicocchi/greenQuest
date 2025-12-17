package com.example.greenquest.apiParameters

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor

@Serializable
enum class TipoResiduo {
    @SerialName("Carton")
    CARTON,

    @SerialName("Plastico")
    PLASTICO,

    @SerialName("Vidrio")
    VIDRIO,

    @SerialName("Metal")
    METAL,

    @SerialName("Papel")
    PAPEL,

    @SerialName("Basura")
    BASURA;

    @OptIn(ExperimentalSerializationApi::class)
    override fun toString() = serializer().descriptor.getElementName(ordinal)
}