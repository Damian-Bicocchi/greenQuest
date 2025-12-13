package com.example.greenquest

import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
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
            UsuarioRepository.refreshToken()
        }

        if (!refreshResponse.isSuccess) {
            runBlocking { tokenStore.clearAllTokens() }
            return null
        }

        runBlocking {
            tokenStore.saveAccessToken(refreshResponse.toString())
        }

        // Reintentar request original con nuevo token
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${refreshResponse.toString()}")
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
