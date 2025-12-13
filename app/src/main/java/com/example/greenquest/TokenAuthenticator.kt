package com.example.greenquest

import com.example.greenquest.apiParameters.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route


class TokenAuthenticator(
    private val tokenStore: TokenDataStore,
    private val api: EstacionService
) : Authenticator {

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {

        // Evitar loops infinitos
        if (responseCount(response) >= 2) return null

        val refreshToken = runBlocking {
            tokenStore.getRefreshToken()
        } ?: return null

        val refreshResponse = runBlocking {
            api.refreshToken(RefreshRequest(refreshToken))
        }

        if (!refreshResponse.isSuccessful) {
            runBlocking { tokenStore.clearAllTokens() }
            return null
        }

        val newAccess = refreshResponse.body()!!.access

        runBlocking {
            tokenStore.saveAccessToken(newAccess)
        }

        // Reintentar request original con nuevo token
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
    }

    private fun responseCount(response: okhttp3.Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
