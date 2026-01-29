package com.example.greenquest

import AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // En local.properties pongan "BASE_URL=http://192.168.0.100:8000/api/" o similar
    // IMPORTANTE: hagan un gradle clean antes de compilar porque sino NO va a compilar.
    private val builder = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
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
                    TokenDataStoreProvider.get(), authApi
                )
            ).build()

    val api: EstacionService = builder.client(client).build().create(EstacionService::class.java)
}
