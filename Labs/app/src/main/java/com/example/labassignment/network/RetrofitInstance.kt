package com.example.labassignment.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/") // Important: ending with /
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}
