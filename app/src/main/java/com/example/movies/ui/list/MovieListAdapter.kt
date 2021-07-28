package com.example.movies.ui.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.movies.model.Movie
import com.example.movies.ui.shared.MovieViewModel

class MovieListAdapter(private val viewModel: MovieViewModel) :
    PagingDataAdapter<Movie, MovieListViewHolder>(MOVIE_COMPARATOR) {

    companion object {
        internal val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder.create(parent, viewModel)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.itemMoviesBinding?.apply {
                movie = repoItem
                executePendingBindings()
            }
        }
    }
}