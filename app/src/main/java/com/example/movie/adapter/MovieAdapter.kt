package com.example.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movie.R
import com.example.movie.databinding.ItemMovieBinding
import com.example.movie.model.Movie

class MovieAdapter(private val onMovieClick: (Movie) -> Unit) : 
    ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        // Add visual feedback
                        binding.root.isPressed = true
                        binding.root.postDelayed({
                            binding.root.isPressed = false
                            onMovieClick(getItem(position))
                        }, 100)
                    } catch (e: Exception) {
                        // Handle any potential errors during click
                        e.printStackTrace()
                    }
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                movieTitle.text = movie.title ?: "Unknown Title"
                movieYear.text = "Year: ${movie.year ?: "N/A"}"
                movieStudio.text = "Type: ${movie.type ?: "N/A"}"
                movieRating.text = "IMDB ID: ${movie.imdbId ?: "N/A"}"

                // Load movie poster with error handling and placeholder
                if (!movie.poster.isNullOrEmpty() && movie.poster != "N/A") {
                    Glide.with(moviePoster)
                        .load(movie.poster)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.ic_movie_placeholder)
                        .error(R.drawable.ic_movie_error)
                        .centerCrop()
                        .override(500, 750)
                        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                        .into(moviePoster)
                } else {
                    // If no poster URL is available, show the error image
                    Glide.with(moviePoster)
                        .load(R.drawable.ic_movie_error)
                        .centerCrop()
                        .into(moviePoster)
                }
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbId == newItem.imdbId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
} 