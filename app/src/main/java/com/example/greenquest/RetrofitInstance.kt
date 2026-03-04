package com.example.greenquest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class InvalidatableLazy<T>(private val initializer: () -> T) : ReadOnlyProperty<Any?, T> {

    private var cached: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (cached == null) cached = initializer()
        @Suppress("UNCHECKED_CAST")
        return cached as T
    }

    fun invalidate() {
        cached = null
    }
}

private fun <T> invalidatableLazy(initializer: () -> T) = InvalidatableLazy(initializer)

object RetrofitInstance {
    fun onApiUrlChanged() {
        _authApi.invalidate()
        _client.invalidate()
        _api.invalidate()
    }
    private val _authApi = invalidatableLazy {
        Retrofit.Builder()
            .baseUrl(GreenQuestApp.apiStorage.getApiURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    private val _client = invalidatableLazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(TokenDataStoreProvider.get()))
            .authenticator(
                TokenAuthenticator(TokenDataStoreProvider.get(), authApi)
            )
            .build()
    }

    private val _api = invalidatableLazy {
        Retrofit.Builder()
            .baseUrl(GreenQuestApp.apiStorage.getApiURL())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(EstacionService::class.java)
    }

    // Variables públicas
    val authApi: AuthService by _authApi
    val client: OkHttpClient by _client
    val api: EstacionService by _api
}


