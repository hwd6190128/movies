package com.example.movies.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

const val API_BASE_URL = "http://api.themoviedb.org"
const val POSTER_URL = "http://image.tmdb.org/t/p"
const val API_KEY = "6753d9119b9627493ae129f3c3c99151"
const val API_QUERY = "superman"

object HttpClient {

    private var sInstance: Webservice? = null

    fun getService(): Webservice {
        if (sInstance == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor {
                    Timber.tag("OkHttp").d(it)
                }.apply {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                })
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build()

            sInstance = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Webservice::class.java)

        }
        return sInstance as Webservice
    }

}