package com.infinum.shows_bruno_sacaric.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://api.infinum.academy/"

object RetrofitClient {

    val baseUrl: String get() {
        return BASE_URL
    }

    private var retrofit: Retrofit? = null
    val retrofitInstance: Retrofit?
    get() {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }
        return retrofit
    }
}