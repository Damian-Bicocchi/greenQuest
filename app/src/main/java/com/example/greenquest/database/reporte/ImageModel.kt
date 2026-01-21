package com.example.greenquest.database.reporte

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.user.User
import java.time.OffsetDateTime

@Entity(
    tableName = "report_images",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["id_usuario_reporte"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HistorialResiduo::class,
            parentColumns = ["historialResiduoId"],
            childColumns = ["id_historial_residuo_reportado"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_usuario_reporte"),
        Index("id_historial_residuo_reportado")
    ]
)
data class ImageModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_imagen_reportada")
    val idImagenReportada: Long = 0,

    @ColumnInfo(name = "image_data")
    val imageData: ByteArray,

    @ColumnInfo(name = "clasificacion_usuario")
    val clasificacionUsuario: TipoResiduo,

    @ColumnInfo(name = "fecha")
    val fecha: OffsetDateTime? = null,

    @ColumnInfo(name = "id_usuario_reporte")
    val idUsuarioReporte: Int,

    @ColumnInfo(name = "id_historial_residuo_reportado")
    val idHistorialResiduoReportado: String
) {
    override fun equals(other: Any?): Boolean {
        // Codigo generado por el IDE
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (idImagenReportada != other.idImagenReportada) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        // Codigo generado por el IDE
        var result = idImagenReportada.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}