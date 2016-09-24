package com.eriktrinh.ikuzo.web

import android.content.Context
import android.util.Log
import com.eriktrinh.ikuzo.utils.AuthUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    val BASE_URL = "https://anilist.co/api/"
    private val TAG = "ServiceGenerator"
    private val httpClient = OkHttpClient.Builder()
    private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    fun <T> createService(serviceClass: Class<T>, context: Context): T {
        if (httpClient.interceptors().isEmpty()) {
            httpClient.addInterceptor { chain ->
                val authToken = AuthUtils.getAccessToken(context)
                val original: Request = chain.request()
                if (authToken == null) {
                    chain.proceed(original)
                } else {
                    val requestBuilder = original.newBuilder()
                            .addHeader("Authorization", authToken)
                            .method(original.method(), original.body())
                    Log.i(TAG, "Added authorization: \"$authToken\" to request \"${original.url()}\"")
                    chain.proceed(requestBuilder.build())
                }
            }
        }
        httpClient.authenticator(TokenAuthenticator(context))

        val client = httpClient.build()
        val retrofit = builder.client(client)
                .build()
        return retrofit.create(serviceClass)
    }

    fun <T> createService(serviceClass: Class<T>): T {
        httpClient.interceptors().removeAll { true }
        val client = httpClient.build()
        val retrofit = builder.client(client)
                .build()
        return retrofit.create(serviceClass)
    }
}
