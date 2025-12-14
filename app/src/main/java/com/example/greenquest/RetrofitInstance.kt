package com.example.greenquest

import AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(TokenDataStoreProvider.get()))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASEURLCELU)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: EstacionService = retrofit.create(EstacionService::class.java)
}
