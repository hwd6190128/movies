package com.example.movies.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ItemFooterProgressBinding

class MovieLoadStateViewHolder(binding: ItemFooterProgressBinding, retry: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    internal var footerProgressBinding: ItemFooterProgressBinding = binding

    fun bind(loadState: LoadState) {
        footerProgressBinding.apply {

            progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            tvErrorMsg.also {
                val isError = loadState is LoadState.Error
                it.text = if (isError) (loadState as LoadState.Error).error.localizedMessage else ""
                it.visibility = if (isError) View.VISIBLE else View.GONE
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
            val binding = ItemFooterProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MovieLoadStateViewHolder(binding, retry)
        }
    }
}