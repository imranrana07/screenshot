package com.test.meldcx.data.api

import com.test.meldcx.utils.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val gson = GsonBuilder().setLenient().create()
    val client = OkHttpClient
        .Builder()
        .addInterceptor(logger)
        .build()

    var retrofit: Retrofit? = null
    get() {
        if (field == null) {
            retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return field
    }
}