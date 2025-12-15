import com.example.greenquest.TokenDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = runBlocking {
            tokenDataStore.getAccessToken()
        }
        return if (accessToken != null) {
            val requestBuilder =
                originalRequest.newBuilder().header("Authorization", "Bearer $accessToken")

            // Por si fuera el caso de que haya más Headers (No pasa).
            originalRequest.headers.forEach { (name, value) ->
                if (name != "Authorization") {
                    requestBuilder.header(name, value)
                }
            }

            chain.proceed(requestBuilder.build())
        } else {
            // Si no hay token, proceder sin él
            chain.proceed(originalRequest)
        }
    }
}

