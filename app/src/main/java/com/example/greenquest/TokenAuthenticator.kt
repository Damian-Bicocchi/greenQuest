package com.example.greenquest

import android.content.Context
import android.content.Intent
import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route


class TokenAuthenticator(
    private val tokenStore: TokenDataStore, private val api: AuthService, private val context: Context
) : Authenticator {
    private var retryCount = 0
    private val maxRetries = 2

    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        synchronized(this) {
            if (retryCount >= maxRetries) {
                logout()
                return null
            }

            retryCount++

            val refreshToken = runBlocking {
                tokenStore.getRefreshToken()
            } ?: return null.also{logout()}

            try {
                val newTokensResponse = runBlocking { api.refreshToken(RefreshRequest(refreshToken)) }
                if (!newTokensResponse.isSuccessful) {
                    logout()
                    return null
                }

                val newToken = newTokensResponse.body()
                if (newToken == null || newToken.access.isEmpty()) {
                    logout()
                    return null
                }

                runBlocking { tokenStore.saveAccessToken(newToken.access) }

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newToken.access}").build()

            } catch (_: Exception) {
                logout()
                return null
            }
        }
    }

    private fun logout() {
        // Refresh expirado, logout
        runBlocking {
            tokenStore.clearAllTokens()
            UsuarioRepository.obtenerUsuarioLocal()
                ?.let { UsuarioRepository.eliminarUsuarioLocal(it) }
        }
        val intent = Intent(context, LauncherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}
