package com.corniland.mobile.data.repository

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import com.corniland.mobile.data.SessionManager
import com.corniland.mobile.data.api.CornilandAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

data class Repositories(
    val project: ProjectRepository = ProjectRepository(),
    val user: UserRepository = UserRepository()
) {

    companion object {
        val api: CornilandAPI = {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.corniland.ovh/")
                .client(generateHttpClient())
                .addConverterFactory(nullOnEmptyConverterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(CornilandAPI::class.java)
        }()

        private fun generateHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        }
    }

}

val nullOnEmptyConverterFactory = object : Converter.Factory() {
    fun converterFactory() = this
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ) = object : Converter<ResponseBody, Any?> {
        val nextResponseBodyConverter =
            retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

        override fun convert(value: ResponseBody) =
            if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
    }
}

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.getJwt()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }

}

val RepositoriesAmbient: ProvidableAmbient<Repositories> = ambientOf()
