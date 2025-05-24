package com.example.movie.repository

import com.example.movie.api.OmdbApiService
import com.example.movie.model.MovieDetail
import com.example.movie.model.MovieSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository {
    private val apiService: OmdbApiService
    private val apiKey = "2671d7db"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(OmdbApiService::class.java)
    }

    suspend fun searchMovies(query: String): MovieSearchResponse {
        return apiService.searchMovies(query, apiKey)
    }

    suspend fun getMovieDetails(imdbId: String): MovieDetail {
        return apiService.getMovieDetails(imdbId, apiKey)
    }
} 