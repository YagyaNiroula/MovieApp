package com.example.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Movie
import com.example.movie.model.MovieDetail
import com.example.movie.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _selectedMovie = MutableLiveData<MovieDetail>()
    val selectedMovie: LiveData<MovieDetail> = _selectedMovie

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = repository.searchMovies(query)
                // Filter to show only movies
                _movies.value = response.search.filter { it.type.equals("movie", ignoreCase = true) }
                if (_movies.value.isNullOrEmpty()) {
                    _error.value = "No movies found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getMovieDetails(imdbId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _selectedMovie.value = repository.getMovieDetails(imdbId)
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 