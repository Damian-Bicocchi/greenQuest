package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp

object ReporteRepository {
    private val imageReportDao by lazy {
        GreenQuestApp.instance.database.imageDao()
    }


}