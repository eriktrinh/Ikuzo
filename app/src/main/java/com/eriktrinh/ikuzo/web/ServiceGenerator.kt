package com.eriktrinh.ikuzo.web

import android.content.Context
import com.eriktrinh.ikuzo.oauth.TokenAuthenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    val BASE_URL = "https://anilist.co/api/"
    private val httpClient = OkHttpClient.Builder()
    private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    fun <T> createService(serviceClass: Class<T>, context: Context, authToken: String): T {
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                        .addHeader("Authorization", authToken)
                        .method(original.method(), original.body())

                chain.proceed(requestBuilder.build())
            }
        httpClient.authenticator(TokenAuthenticator(context))

        val client = httpClient.build()
        val retrofit = builder.client(client)
                .build()
        return retrofit.create(serviceClass)
    }

    fun <T> createService(serviceClass: Class<T>): T {
        val client = httpClient.build()
        val retrofit = builder.client(client)
                .build()
        return retrofit.create(serviceClass)
    }
}
