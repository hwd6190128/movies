package com.example.movies.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.Movie

class MovieAdapter(private val mViewModel: MovieListViewModel) :
    PagingDataAdapter<Movie, MovieViewHolder>(MOVIE_COMPARATOR) {

    companion object {
        internal val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mViewModel
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.itemMoviesBinding?.apply {
                movie = repoItem
                executePendingBindings()
            }

        }
    }
}