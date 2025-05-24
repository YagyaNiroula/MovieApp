package com.example.movie

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movie.databinding.ActivityMovieDetailsBinding
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.model.MovieDetail

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadMovieDetails()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Movie Details"
        }
    }

    private fun loadMovieDetails() {
        val imdbId = intent.getStringExtra(EXTRA_IMDB_ID)
        if (imdbId != null) {
            viewModel.getMovieDetails(imdbId)
        } else {
            Toast.makeText(this, "Error: Movie ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.selectedMovie.observe(this) { movie: MovieDetail? ->
            movie?.let {
                binding.apply {
                    // Load movie poster
                    if (!it.poster.isNullOrEmpty() && it.poster != "N/A") {
                        Glide.with(moviePoster)
                            .load(it.poster)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .centerCrop()
                            .override(800, 1200) // Larger size for details view
                            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                            .into(moviePoster)
                    }
                    movieTitle.text = it.title
                    movieYear.text = "Year: ${it.year}"
                    movieRated.text = "Rated: ${it.rated}"
                    movieReleased.text = "Released: ${it.released}"
                    movieRuntime.text = "Runtime: ${it.runtime}"
                    movieGenre.text = "Genre: ${it.genre}"
                    movieDirector.text = "Director: ${it.director}"
                    movieWriter.text = "Writer: ${it.writer}"
                    movieActors.text = "Actors: ${it.actors}"
                    moviePlot.text = "Plot: ${it.plot}"
                    movieLanguage.text = "Language: ${it.language}"
                    movieCountry.text = "Country: ${it.country}"
                    movieAwards.text = "Awards: ${it.awards}"
                    movieRatings.text = "Ratings:\n${it.ratings?.joinToString("\n") { rating ->
                        "${rating.source}: ${rating.value}"
                    }}"
                    movieMetascore.text = "Metascore: ${it.metascore}"
                    movieImdbRating.text = "IMDB Rating: ${it.imdbRating}"
                    movieImdbVotes.text = "IMDB Votes: ${it.imdbVotes}"
                    movieType.text = "Type: ${it.type}"
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_IMDB_ID = "extra_imdb_id"
    }
} 