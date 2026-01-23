package com.example.greenquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.greenquest.database.reporte.ReporteData
import com.example.greenquest.database.reporte.ImageWithDetails

@Dao
interface ImageReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(reporteData: ReporteData)

    @Transaction
    @Query("""
        SELECT * 
        FROM report_images 
        WHERE id_imagen_reportada = :imageId
    """)
    suspend fun getImageWithDetails(imageId: Long): ImageWithDetails?


}