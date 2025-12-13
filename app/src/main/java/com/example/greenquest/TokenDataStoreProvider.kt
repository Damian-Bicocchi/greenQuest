package com.example.greenquest

import android.content.Context

object TokenDataStoreProvider{
    private lateinit var tokenDataStore: TokenDataStore

    fun init(context: Context) {
        tokenDataStore = TokenDataStore(context.dataStore)
    }

    fun get(): TokenDataStore = tokenDataStore
}