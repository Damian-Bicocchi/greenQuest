package com.example.greenquest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASEURLCELU)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: EstacionService = retrofit.create(EstacionService::class.java)

}