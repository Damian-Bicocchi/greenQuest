package com.example.greenquest

import AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val builder = Retrofit.Builder().baseUrl(Constants.BASEURLCELU)
        .addConverterFactory(GsonConverterFactory.create())

    val authApi: AuthService = builder.build().create(AuthService::class.java)

    private val client =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(TokenDataStoreProvider.get()))
            .authenticator(
                TokenAuthenticator(
                    TokenDataStoreProvider.get(), authApi, GreenQuestApp.instance
                )
            ).build()

    val api: EstacionService = builder.client(client).build().create(EstacionService::class.java)
}
